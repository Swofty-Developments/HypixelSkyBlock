package net.swofty.type.ravengardgeneric.texturepack;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.map.MapColors;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Arrays;

final class TexturePackWorldRasterRenderer {
    private static final int OVERLAY_DRAW_ALPHA_THRESHOLD = 16;
    private static final int MASK_OPEN_ALPHA_THRESHOLD = 1;
    private static final int PLAYER_ICON_ALPHA_THRESHOLD = 32;
    private static final int PLAYER_ICON_TARGET_SIZE = 14;
    private static final double PLAYER_ICON_HEADING_OFFSET_DEGREES = 180.0;

    private static final OverlayData OVERLAY_DATA = loadOverlayData();
    private static final int[] MINIMAP_OVERLAY = OVERLAY_DATA.overlayColors();
    private static final boolean[] MINIMAP_CONTENT_MASK = OVERLAY_DATA.contentMask();
    private static final BufferedImage PLAYER_ICON = loadPlayerIcon();

    void renderWorld(byte[] colors, Instance instance, Pos center) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        if (colors.length < mapSize * mapSize) {
            return;
        }

        int halfMap = mapSize / 2;
        int startX = center.blockX() - halfMap;
        int startZ = center.blockZ() - halfMap;

        for (int pz = 0; pz < mapSize; pz++) {
            for (int px = 0; px < mapSize; px++) {
                int worldX = startX + px;
                int worldZ = startZ + pz;

                if (!instance.isChunkLoaded(worldX >> 4, worldZ >> 4)) {
                    colors[pz * mapSize + px] = 0;
                    continue;
                }

                int surfaceY = findSurfaceY(instance, worldX, worldZ);
                Block block = instance.getBlock(worldX, surfaceY, worldZ);
                colors[pz * mapSize + px] = BlockColorMapping.getMapColor(block);
            }
        }
    }

    void applyStandardOverlay(byte[] colors) {
        applyContentMask(colors);
        applyFrameOverlay(colors);
    }

    void drawFacingArrow(byte[] colors, float yaw) {
        if (PLAYER_ICON != null) {
            drawPlayerIcon(colors, yaw);
            return;
        }

        drawFallbackArrow(colors, yaw);
    }

    private static void drawFallbackArrow(byte[] colors, float yaw) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        int centerX = mapSize / 2;
        int centerY = mapSize / 2;

        double radians = Math.toRadians(yaw);
        double dirX = -Math.sin(radians);
        double dirY = Math.cos(radians);
        double perpX = -dirY;
        double perpY = dirX;

        double tipDistance = 8.0;
        double backDistance = 3.0;
        double halfWidth = 2.4;
        double tailDistance = 5.0;

        int tipX = (int) Math.round(centerX + dirX * tipDistance);
        int tipY = (int) Math.round(centerY + dirY * tipDistance);
        int leftX = (int) Math.round(centerX - dirX * backDistance + perpX * halfWidth);
        int leftY = (int) Math.round(centerY - dirY * backDistance + perpY * halfWidth);
        int rightX = (int) Math.round(centerX - dirX * backDistance - perpX * halfWidth);
        int rightY = (int) Math.round(centerY - dirY * backDistance - perpY * halfWidth);
        int tailX = (int) Math.round(centerX - dirX * tailDistance);
        int tailY = (int) Math.round(centerY - dirY * tailDistance);

        byte outlineColor = MapColors.COLOR_BLACK.multiply86();
        byte fillColor = MapColors.COLOR_RED.baseColor();

        drawLine(colors, centerX, centerY, tailX, tailY, outlineColor);
        drawFilledTriangle(colors, tipX, tipY, leftX, leftY, rightX, rightY, fillColor);
        drawLine(colors, tipX, tipY, leftX, leftY, outlineColor);
        drawLine(colors, tipX, tipY, rightX, rightY, outlineColor);
        drawLine(colors, leftX, leftY, rightX, rightY, outlineColor);
    }

    private static void drawPlayerIcon(byte[] colors, float yaw) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        int centerX = mapSize / 2;
        int centerY = mapSize / 2;
        int iconWidth = PLAYER_ICON.getWidth();
        int iconHeight = PLAYER_ICON.getHeight();
        int originX = centerX - (iconWidth / 2);
        int originY = centerY - (iconHeight / 2);

        double radians = Math.toRadians(yaw + PLAYER_ICON_HEADING_OFFSET_DEGREES);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double iconCenterX = (iconWidth - 1) * 0.5;
        double iconCenterY = (iconHeight - 1) * 0.5;

        for (int dy = 0; dy < iconHeight; dy++) {
            for (int dx = 0; dx < iconWidth; dx++) {
                double relX = dx - iconCenterX;
                double relY = dy - iconCenterY;

                double srcX = (cos * relX) + (sin * relY) + iconCenterX;
                double srcY = (-sin * relX) + (cos * relY) + iconCenterY;

                int sampleX = (int) Math.round(srcX);
                int sampleY = (int) Math.round(srcY);
                if (sampleX < 0 || sampleY < 0 || sampleX >= iconWidth || sampleY >= iconHeight) {
                    continue;
                }

                int argb = PLAYER_ICON.getRGB(sampleX, sampleY);
                int alpha = (argb >>> 24) & 0xFF;
                if (alpha < PLAYER_ICON_ALPHA_THRESHOLD) {
                    continue;
                }

                int red = (argb >>> 16) & 0xFF;
                int green = (argb >>> 8) & 0xFF;
                int blue = argb & 0xFF;
                int rgb = (red << 16) | (green << 8) | blue;
                byte mapColor = (byte) Byte.toUnsignedInt(MapColors.closestColor(rgb).getIndex());
                setColor(colors, originX + dx, originY + dy, mapColor);
            }
        }
    }

    private int findSurfaceY(Instance instance, int x, int z) {
        int maxY = instance.getCachedDimensionType().maxY();
        int minY = instance.getCachedDimensionType().minY();

        for (int y = maxY; y >= minY; y--) {
            Block block = instance.getBlock(x, y, z);
            if (!block.isAir() && block.id() != Block.CAVE_AIR.id()) {
                return y;
            }
        }
        return minY;
    }

    private static void applyFrameOverlay(byte[] colors) {
        for (int i = 0; i < colors.length; i++) {
            int overlayColor = MINIMAP_OVERLAY[i];
            if (overlayColor >= 0) {
                colors[i] = (byte) overlayColor;
            }
        }
    }

    private static void applyContentMask(byte[] colors) {
        for (int i = 0; i < colors.length; i++) {
            if (!MINIMAP_CONTENT_MASK[i]) {
                colors[i] = 0;
            }
        }
    }

    private static OverlayData loadOverlayData() {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        int[] overlayColors = new int[mapSize * mapSize];
        Arrays.fill(overlayColors, -1);
        boolean[] contentMask = new boolean[mapSize * mapSize];
        Arrays.fill(contentMask, true);

        try (InputStream input = TexturePackWorldRasterRenderer.class.getClassLoader()
                .getResourceAsStream("minimap/minimap_overlay.png")) {
            if (input == null) {
                return new OverlayData(overlayColors, contentMask);
            }

            BufferedImage image = ImageIO.read(input);
            if (image == null) {
                return new OverlayData(overlayColors, contentMask);
            }
            image = normalizeOverlaySize(image);

            int[] alphaMap = new int[mapSize * mapSize];
            for (int y = 0; y < mapSize; y++) {
                for (int x = 0; x < mapSize; x++) {
                    int index = y * mapSize + x;
                    int argb = image.getRGB(x, y);
                    int alpha = (argb >>> 24) & 0xFF;
                    alphaMap[index] = alpha;
                    if (alpha < OVERLAY_DRAW_ALPHA_THRESHOLD) {
                        continue;
                    }

                    int red = (argb >>> 16) & 0xFF;
                    int green = (argb >>> 8) & 0xFF;
                    int blue = argb & 0xFF;
                    int rgb = (red << 16) | (green << 8) | blue;
                    overlayColors[index] = Byte.toUnsignedInt(MapColors.closestColor(rgb).getIndex());
                }
            }

            boolean[] interior = buildInteriorMask(alphaMap);
            if (hasTrue(interior)) {
                contentMask = expandMask(interior, 2);
            }
        } catch (Exception ignored) {
        }

        return new OverlayData(overlayColors, contentMask);
    }

    private static BufferedImage loadPlayerIcon() {
        BufferedImage source = tryLoadPlayerIconFromClasspath();
        if (source == null) {
            return null;
        }

        int width = Math.max(1, source.getWidth());
        int height = Math.max(1, source.getHeight());
        int targetWidth;
        int targetHeight;
        if (width >= height) {
            targetWidth = PLAYER_ICON_TARGET_SIZE;
            targetHeight = Math.max(1, (height * PLAYER_ICON_TARGET_SIZE) / width);
        } else {
            targetHeight = PLAYER_ICON_TARGET_SIZE;
            targetWidth = Math.max(1, (width * PLAYER_ICON_TARGET_SIZE) / height);
        }

        BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaled.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(source, 0, 0, targetWidth, targetHeight, null);
        } finally {
            graphics.dispose();
        }
        return scaled;
    }

    private static BufferedImage tryLoadPlayerIconFromClasspath() {
        try (InputStream input = TexturePackWorldRasterRenderer.class.getClassLoader().getResourceAsStream("texturepack/player_marker.png")) {
            if (input == null) {
                return null;
            }
            return ImageIO.read(input);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static BufferedImage normalizeOverlaySize(BufferedImage source) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        if (source.getWidth() == mapSize && source.getHeight() == mapSize) {
            return source;
        }

        BufferedImage output = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = output.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(source, 0, 0, mapSize, mapSize, null);
        } finally {
            graphics.dispose();
        }
        return output;
    }

    private static boolean[] buildInteriorMask(int[] alphaMap) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        boolean[] interior = new boolean[mapSize * mapSize];

        int centerX = mapSize / 2;
        int centerY = mapSize / 2;
        int centerIndex = centerY * mapSize + centerX;
        if (alphaMap[centerIndex] > MASK_OPEN_ALPHA_THRESHOLD) {
            return interior;
        }

        int[] queue = new int[mapSize * mapSize];
        int head = 0;
        int tail = 0;
        queue[tail++] = centerIndex;
        interior[centerIndex] = true;

        while (head < tail) {
            int index = queue[head++];
            int x = index % mapSize;
            int y = index / mapSize;

            if (x > 0) {
                tail = enqueueIfOpen(alphaMap, interior, queue, tail, index - 1);
            }
            if (x < mapSize - 1) {
                tail = enqueueIfOpen(alphaMap, interior, queue, tail, index + 1);
            }
            if (y > 0) {
                tail = enqueueIfOpen(alphaMap, interior, queue, tail, index - mapSize);
            }
            if (y < mapSize - 1) {
                tail = enqueueIfOpen(alphaMap, interior, queue, tail, index + mapSize);
            }
        }
        return interior;
    }

    private static int enqueueIfOpen(int[] alphaMap, boolean[] interior, int[] queue, int tail, int index) {
        if (interior[index] || alphaMap[index] > MASK_OPEN_ALPHA_THRESHOLD) {
            return tail;
        }
        interior[index] = true;
        queue[tail] = index;
        return tail + 1;
    }

    private static boolean hasTrue(boolean[] values) {
        for (boolean value : values) {
            if (value) {
                return true;
            }
        }
        return false;
    }

    private static boolean[] expandMask(boolean[] source, int radius) {
        if (radius <= 0) {
            return source;
        }

        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        boolean[] expanded = Arrays.copyOf(source, source.length);
        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                int index = y * mapSize + x;
                if (source[index]) {
                    continue;
                }

                boolean nearInterior = false;
                for (int dy = -radius; dy <= radius && !nearInterior; dy++) {
                    int ny = y + dy;
                    if (ny < 0 || ny >= mapSize) {
                        continue;
                    }
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        if (nx < 0 || nx >= mapSize) {
                            continue;
                        }
                        if (source[ny * mapSize + nx]) {
                            nearInterior = true;
                            break;
                        }
                    }
                }
                expanded[index] = nearInterior;
            }
        }
        return expanded;
    }

    private static void drawFilledTriangle(byte[] colors, int ax, int ay, int bx, int by, int cx, int cy, byte color) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        int minX = Math.max(0, Math.min(ax, Math.min(bx, cx)));
        int maxX = Math.min(mapSize - 1, Math.max(ax, Math.max(bx, cx)));
        int minY = Math.max(0, Math.min(ay, Math.min(by, cy)));
        int maxY = Math.min(mapSize - 1, Math.max(ay, Math.max(by, cy)));

        double area = edge(ax, ay, bx, by, cx, cy);
        if (Math.abs(area) < 0.0001) {
            return;
        }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                double w0 = edge(bx, by, cx, cy, x, y);
                double w1 = edge(cx, cy, ax, ay, x, y);
                double w2 = edge(ax, ay, bx, by, x, y);
                boolean hasNeg = (w0 < 0) || (w1 < 0) || (w2 < 0);
                boolean hasPos = (w0 > 0) || (w1 > 0) || (w2 > 0);
                if (!(hasNeg && hasPos)) {
                    setColor(colors, x, y, color);
                }
            }
        }
    }

    private static double edge(int ax, int ay, int bx, int by, int px, int py) {
        return (double) (px - ax) * (by - ay) - (double) (py - ay) * (bx - ax);
    }

    private static void drawLine(byte[] colors, int x0, int y0, int x1, int y1, byte color) {
        int dx = Math.abs(x1 - x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0);
        int sy = y0 < y1 ? 1 : -1;
        int error = dx + dy;

        int x = x0;
        int y = y0;
        while (true) {
            setColor(colors, x, y, color);
            if (x == x1 && y == y1) {
                break;
            }
            int e2 = 2 * error;
            if (e2 >= dy) {
                error += dy;
                x += sx;
            }
            if (e2 <= dx) {
                error += dx;
                y += sy;
            }
        }
    }

    private static void setColor(byte[] colors, int x, int y, byte color) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        if (x < 0 || y < 0 || x >= mapSize || y >= mapSize) {
            return;
        }
        colors[y * mapSize + x] = color;
    }

    private record OverlayData(
            int[] overlayColors,
            boolean[] contentMask
    ) {
    }
}
