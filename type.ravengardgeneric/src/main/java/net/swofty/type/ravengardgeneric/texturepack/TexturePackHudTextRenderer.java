package net.swofty.type.ravengardgeneric.texturepack;

import net.minestom.server.map.MapColors;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Arrays;

final class TexturePackHudTextRenderer {
    private static final int ASCII_GLYPH_SIZE = 8;
    private static final int ASCII_SHEET_COLUMNS = 16;
    private static final int SPACE_ADVANCE = 4;
    private static final int DEFAULT_ADVANCE = 6;
    private static final int HUD_ALPHA_THRESHOLD = 32;
    private static final char UNKNOWN_CHAR = '?';

    private static final Color HUD_SERVER_COLOR = new Color(85, 85, 85);
    private static final Color HUD_DATE_COLOR = new Color(170, 170, 170);
    private static final Color HUD_DOMAIN_COLOR = new Color(255, 255, 255);
    private static final Color HUD_TITLE_COLOR = new Color(255, 255, 85);
    private static final Color HUD_ICON_COLOR = new Color(255, 255, 85);
    private static final Color HUD_VALUE_COLOR = new Color(255, 255, 255);
    private static final Color HUD_SHADOW_COLOR = new Color(0, 0, 0, 220);

    private static final String[] ICON_HOURGLASS = {
            "#######",
            " ##### ",
            "  ###  ",
            "   #   ",
            "  ###  ",
            " ##### ",
            "#######"
    };
    private static final String[] ICON_SWORD = {
            "    #  ",
            "   ##  ",
            "  ##   ",
            " ##    ",
            "##     ",
            " #     ",
            "###    "
    };
    private static final String[] ICON_PERSON = {
            "  ###  ",
            "  ###  ",
            "   #   ",
            " ##### ",
            "   #   ",
            "  # #  ",
            " ## ## "
    };
    private static final String[] ICON_SQUAD = {
            " # # # ",
            " # # # ",
            "#######",
            " ##### ",
            "#######",
            " # # # ",
            "# # # #"
    };

    private static final Object CACHE_LOCK = new Object();
    private static volatile HudTextRaster cachedHudText;
    private static volatile StatsRaster cachedStatsRaster;
    private static volatile TopBannerRaster cachedTopBannerRaster;
    private static final FontSheet FONT_SHEET = loadFontSheet();

    void drawServerHudText(byte[] colors, String shortServerName, String date, String domainText) {
        if (shortServerName == null || shortServerName.isBlank()) {
            shortServerName = "unknown";
        }
        if (date == null || date.isBlank()) {
            date = "0000-00-00";
        }
        if (domainText == null || domainText.isBlank()) {
            domainText = "mc.hypixel.net";
        }

        HudTextRaster raster = getHudTextRaster(shortServerName, date, domainText);
        for (int i = 0; i < colors.length; i++) {
            if (raster.mask[i]) {
                colors[i] = raster.mapColors[i];
            }
        }
    }

    void drawTopBannerText(byte[] colors, String title) {
        TopBannerRaster topBannerRaster = getTopBannerRaster(title);
        for (int i = 0; i < colors.length; i++) {
            if (topBannerRaster.mask[i]) {
                colors[i] = topBannerRaster.mapColors[i];
            }
        }
    }

    void drawStatsHud(byte[] colors, String timer, int swords, int players, int squads) {
        StatsRaster raster = getStatsRaster(timer, swords, players, squads);
        for (int i = 0; i < colors.length; i++) {
            if (raster.mask[i]) {
                colors[i] = raster.mapColors[i];
            }
        }
    }

    private static HudTextRaster getHudTextRaster(String shortServerName, String date, String domainText) {
        HudTextRaster local = cachedHudText;
        if (local != null
                && local.shortServerName.equals(shortServerName)
                && local.date.equals(date)
                && local.domainText.equals(domainText)) {
            return local;
        }

        synchronized (CACHE_LOCK) {
            local = cachedHudText;
            if (local != null
                    && local.shortServerName.equals(shortServerName)
                    && local.date.equals(date)
                    && local.domainText.equals(domainText)) {
                return local;
            }

            HudTextRaster built = buildHudTextRaster(shortServerName, date, domainText);
            cachedHudText = built;
            return built;
        }
    }

    private static HudTextRaster buildHudTextRaster(String shortServerName, String date, String domainText) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
        int rightEdge = mapSize - 2;

        String topPrefix = date + " ";
        String topLine = topPrefix + shortServerName;
        int topWidth = textWidth(topLine);
        int topX = rightEdge - topWidth;
        int topY = mapSize - 23;
        int serverX = topX + textWidth(topPrefix);

        int bottomX = rightEdge - textWidth(domainText);
        int bottomY = mapSize - 10;

        drawShadowedText(image, date, topX, topY, HUD_DATE_COLOR);
        drawShadowedText(image, shortServerName, serverX, topY, HUD_SERVER_COLOR);
        drawShadowedText(image, domainText, bottomX, bottomY, HUD_DOMAIN_COLOR);

        return rasterizeImage(shortServerName, date, domainText, image);
    }

    private static TopBannerRaster getTopBannerRaster(String title) {
        if (title == null || title.isBlank()) {
            title = "RAVENGARD";
        }

        TopBannerRaster local = cachedTopBannerRaster;
        if (local != null && local.title.equals(title)) {
            return local;
        }

        synchronized (CACHE_LOCK) {
            local = cachedTopBannerRaster;
            if (local != null && local.title.equals(title)) {
                return local;
            }

            TopBannerRaster built = buildTopBannerRaster(title);
            cachedTopBannerRaster = built;
            return built;
        }
    }

    private static TopBannerRaster buildTopBannerRaster(String title) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);

        int titleWidth = textWidthBold(title);
        int titleX = (mapSize - titleWidth) / 2;
        int titleY = 0;
        drawShadowedBoldText(image, title, titleX, titleY, HUD_TITLE_COLOR);

        RasterizedMap raster = rasterizeImage(image);
        return new TopBannerRaster(title, raster.mapColors(), raster.mask());
    }

    private static StatsRaster getStatsRaster(String timer, int swords, int players, int squads) {
        if (timer == null || timer.isBlank()) {
            timer = "99:99";
        }

        StatsRaster local = cachedStatsRaster;
        if (local != null
                && local.timer.equals(timer)
                && local.swords == swords
                && local.players == players
                && local.squads == squads) {
            return local;
        }

        synchronized (CACHE_LOCK) {
            local = cachedStatsRaster;
            if (local != null
                    && local.timer.equals(timer)
                    && local.swords == swords
                    && local.players == players
                    && local.squads == squads) {
                return local;
            }

            StatsRaster built = buildStatsRaster(timer, swords, players, squads);
            cachedStatsRaster = built;
            return built;
        }
    }

    private static StatsRaster buildStatsRaster(String timer, int swords, int players, int squads) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);

        String swordText = Integer.toString(Math.max(swords, 0));
        String playerText = Integer.toString(Math.max(players, 0));
        String squadText = Integer.toString(Math.max(squads, 0));

        int segmentGap = 6;
        int iconGap = 2;
        int baselineY = 2;

        int hourglassWidth = patternWidth(ICON_HOURGLASS) + iconGap + textWidth(timer);
        int swordWidth = patternWidth(ICON_SWORD) + iconGap + textWidth(swordText);
        int playerWidth = patternWidth(ICON_PERSON) + iconGap + textWidth(playerText);
        int squadWidth = patternWidth(ICON_SQUAD) + iconGap + textWidth(squadText);
        int totalWidth = hourglassWidth + segmentGap + swordWidth + segmentGap + playerWidth + segmentGap + squadWidth;
        int x = Math.max(0, (mapSize - totalWidth) / 2);

        drawPatternIcon(image, x, baselineY, ICON_HOURGLASS, HUD_ICON_COLOR);
        x += patternWidth(ICON_HOURGLASS) + iconGap;
        drawShadowedText(image, timer, x, baselineY, HUD_VALUE_COLOR);
        x += textWidth(timer) + segmentGap;

        drawPatternIcon(image, x, baselineY, ICON_SWORD, HUD_ICON_COLOR);
        x += patternWidth(ICON_SWORD) + iconGap;
        drawShadowedText(image, swordText, x, baselineY, HUD_VALUE_COLOR);
        x += textWidth(swordText) + segmentGap;

        drawPatternIcon(image, x, baselineY, ICON_PERSON, HUD_ICON_COLOR);
        x += patternWidth(ICON_PERSON) + iconGap;
        drawShadowedText(image, playerText, x, baselineY, HUD_VALUE_COLOR);
        x += textWidth(playerText) + segmentGap;

        drawPatternIcon(image, x, baselineY, ICON_SQUAD, HUD_ICON_COLOR);
        x += patternWidth(ICON_SQUAD) + iconGap;
        drawShadowedText(image, squadText, x, baselineY, HUD_VALUE_COLOR);

        RasterizedMap raster = rasterizeImage(image);
        return new StatsRaster(timer, swords, players, squads, raster.mapColors(), raster.mask());
    }

    private static HudTextRaster rasterizeImage(
            String shortServerName,
            String date,
            String domainText,
            BufferedImage image
    ) {
        RasterizedMap raster = rasterizeImage(image);
        return new HudTextRaster(shortServerName, date, domainText, raster.mapColors(), raster.mask());
    }

    private static RasterizedMap rasterizeImage(BufferedImage image) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        byte[] mapColors = new byte[mapSize * mapSize];
        boolean[] mask = new boolean[mapSize * mapSize];
        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                int index = y * mapSize + x;
                int argb = image.getRGB(x, y);
                int alpha = (argb >>> 24) & 0xFF;
                if (alpha < HUD_ALPHA_THRESHOLD) {
                    continue;
                }

                int red = (argb >>> 16) & 0xFF;
                int green = (argb >>> 8) & 0xFF;
                int blue = argb & 0xFF;
                int rgb = (red << 16) | (green << 8) | blue;
                mapColors[index] = (byte) Byte.toUnsignedInt(MapColors.closestColor(rgb).getIndex());
                mask[index] = true;
            }
        }
        return new RasterizedMap(mapColors, mask);
    }

    private static void drawShadowedText(BufferedImage image, String text, int x, int y, Color color) {
        drawText(image, text, x + 1, y + 1, HUD_SHADOW_COLOR);
        drawText(image, text, x, y, color);
    }

    private static void drawShadowedBoldText(BufferedImage image, String text, int x, int y, Color color) {
        drawBoldText(image, text, x + 1, y + 1, HUD_SHADOW_COLOR);
        drawBoldText(image, text, x, y, color);
    }

    private static void drawPatternIcon(BufferedImage image, int x, int y, String[] pattern, Color color) {
        for (int py = 0; py < pattern.length; py++) {
            String row = pattern[py];
            for (int px = 0; px < row.length(); px++) {
                if (row.charAt(px) == '#') {
                    setArgb(image, x + px + 1, y + py + 1, HUD_SHADOW_COLOR.getRGB());
                }
            }
        }

        int iconArgb = (color.getAlpha() << 24) | (color.getRGB() & 0x00FFFFFF);
        for (int py = 0; py < pattern.length; py++) {
            String row = pattern[py];
            for (int px = 0; px < row.length(); px++) {
                if (row.charAt(px) == '#') {
                    setArgb(image, x + px, y + py, iconArgb);
                }
            }
        }
    }

    private static int drawText(BufferedImage image, String text, int x, int y, Color color) {
        int advance = 0;
        for (int i = 0; i < text.length(); i++) {
            advance += drawGlyph(image, text.charAt(i), x + advance, y, color);
        }
        return advance;
    }

    private static int drawBoldText(BufferedImage image, String text, int x, int y, Color color) {
        int advance = 0;
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            int codePoint = mapGlyphCode(character);
            int glyphAdvance = drawGlyph(image, character, x + advance, y, color);
            if (codePoint != ' ') {
                drawGlyph(image, character, x + advance + 1, y, color);
                advance += 1;
            }
            advance += glyphAdvance;
        }
        return advance;
    }

    private static int drawGlyph(BufferedImage image, char character, int x, int y, Color color) {
        int codePoint = mapGlyphCode(character);
        int glyphAdvance = FONT_SHEET.advances[codePoint];
        if (codePoint == ' ') {
            return glyphAdvance;
        }

        int cellX = (codePoint % ASCII_SHEET_COLUMNS) * ASCII_GLYPH_SIZE;
        int cellY = (codePoint / ASCII_SHEET_COLUMNS) * ASCII_GLYPH_SIZE;
        for (int gy = 0; gy < ASCII_GLYPH_SIZE; gy++) {
            for (int gx = 0; gx < ASCII_GLYPH_SIZE; gx++) {
                int argb = FONT_SHEET.image.getRGB(cellX + gx, cellY + gy);
                int sourceAlpha = (argb >>> 24) & 0xFF;
                if (sourceAlpha < HUD_ALPHA_THRESHOLD) {
                    continue;
                }
                int outAlpha = (sourceAlpha * color.getAlpha()) / 255;
                int tinted = (outAlpha << 24) | (color.getRGB() & 0x00FFFFFF);
                blendArgb(image, x + gx, y + gy, tinted);
            }
        }
        return glyphAdvance;
    }

    private static int textWidth(String text) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += FONT_SHEET.advances[mapGlyphCode(text.charAt(i))];
        }
        return width;
    }

    private static int textWidthBold(String text) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            int codePoint = mapGlyphCode(text.charAt(i));
            width += FONT_SHEET.advances[codePoint];
            if (codePoint != ' ') {
                width += 1;
            }
        }
        return width;
    }

    private static int patternWidth(String[] pattern) {
        int width = 0;
        for (String row : pattern) {
            width = Math.max(width, row.length());
        }
        return width;
    }

    private static int mapGlyphCode(char character) {
        int code = character;
        if (code == ' ' || (code >= 0 && code < 256)) {
            return code;
        }
        return UNKNOWN_CHAR;
    }

    private static FontSheet loadFontSheet() {
        BufferedImage image = null;
        try (InputStream input = TexturePackHudTextRenderer.class.getClassLoader().getResourceAsStream("minimap/mc_ascii.png")) {
            if (input != null) {
                image = ImageIO.read(input);
            }
        } catch (Exception ignored) {
        }

        if (image == null || image.getWidth() < 128 || image.getHeight() < 128) {
            image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        }

        int[] advances = new int[256];
        Arrays.fill(advances, DEFAULT_ADVANCE);
        advances[' '] = SPACE_ADVANCE;

        for (int code = 0; code < 256; code++) {
            if (code == ' ') {
                continue;
            }

            int cellX = (code % ASCII_SHEET_COLUMNS) * ASCII_GLYPH_SIZE;
            int cellY = (code / ASCII_SHEET_COLUMNS) * ASCII_GLYPH_SIZE;
            int rightmost = -1;
            for (int gx = 0; gx < ASCII_GLYPH_SIZE; gx++) {
                for (int gy = 0; gy < ASCII_GLYPH_SIZE; gy++) {
                    int alpha = (image.getRGB(cellX + gx, cellY + gy) >>> 24) & 0xFF;
                    if (alpha >= HUD_ALPHA_THRESHOLD) {
                        rightmost = Math.max(rightmost, gx);
                    }
                }
            }

            if (rightmost >= 0) {
                advances[code] = Math.min(rightmost + 2, ASCII_GLYPH_SIZE);
            }
        }
        advances[UNKNOWN_CHAR] = Math.max(advances[UNKNOWN_CHAR], DEFAULT_ADVANCE);
        return new FontSheet(image, advances);
    }

    private static void setArgb(BufferedImage image, int x, int y, int argb) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        if (x < 0 || y < 0 || x >= mapSize || y >= mapSize) {
            return;
        }
        image.setRGB(x, y, argb);
    }

    private static void blendArgb(BufferedImage image, int x, int y, int sourceArgb) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        if (x < 0 || y < 0 || x >= mapSize || y >= mapSize) {
            return;
        }

        int destArgb = image.getRGB(x, y);
        int sa = (sourceArgb >>> 24) & 0xFF;
        if (sa <= 0) {
            return;
        }
        if (sa >= 255) {
            image.setRGB(x, y, sourceArgb);
            return;
        }

        int da = (destArgb >>> 24) & 0xFF;
        int sr = (sourceArgb >>> 16) & 0xFF;
        int sg = (sourceArgb >>> 8) & 0xFF;
        int sb = sourceArgb & 0xFF;
        int dr = (destArgb >>> 16) & 0xFF;
        int dg = (destArgb >>> 8) & 0xFF;
        int db = destArgb & 0xFF;

        int outA = sa + ((da * (255 - sa)) / 255);
        int outR = ((sr * sa) + (dr * da * (255 - sa) / 255)) / Math.max(outA, 1);
        int outG = ((sg * sa) + (dg * da * (255 - sa) / 255)) / Math.max(outA, 1);
        int outB = ((sb * sa) + (db * da * (255 - sa) / 255)) / Math.max(outA, 1);

        image.setRGB(x, y, (outA << 24) | (outR << 16) | (outG << 8) | outB);
    }

    private record HudTextRaster(
            String shortServerName,
            String date,
            String domainText,
            byte[] mapColors,
            boolean[] mask
    ) {
    }

    private record TopBannerRaster(
            String title,
            byte[] mapColors,
            boolean[] mask
    ) {
    }

    private record StatsRaster(
            String timer,
            int swords,
            int players,
            int squads,
            byte[] mapColors,
            boolean[] mask
    ) {
    }

    private record FontSheet(
            BufferedImage image,
            int[] advances
    ) {
    }

    private record RasterizedMap(
            byte[] mapColors,
            boolean[] mask
    ) {
    }
}
