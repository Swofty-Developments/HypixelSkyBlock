package net.swofty.type.ravengardgeneric.hud;

import net.swofty.type.ravengardgeneric.gui.RavengardFont;

public final class RavengardHudGlyphs {
    public static final String KEYBIND_INVENTORY = RavengardFont.glyph(0xE006);
    public static final String KEYBIND_MAP = RavengardFont.glyph(0xE007);
    public static final String TEAMMATE_HEALTH_BACKGROUND = RavengardFont.glyph(0xE008);
    public static final String TEAMMATE_HEALTH_BACKGROUND_ALT = RavengardFont.glyph(0xE009);
    public static final String TEAMMATE_HEALTH_FILL = RavengardFont.glyph(0xE00A);
    public static final String QUEUE = RavengardFont.glyph(0xE00D);

    public static final String ICON_TIME = RavengardFont.glyph(0xE002);
    public static final String ICON_TEAM = RavengardFont.glyph(0xE003);
    public static final String ICON_PORTAL = RavengardFont.glyph(0xE004);
    public static final String ICON_PERSON = RavengardFont.glyph(0xE005);
    public static final String ICON_KILL = RavengardFont.glyph(0xE00E);

    public static final String MINIMAP_BACKGROUND = RavengardFont.glyph(0xE100);
    public static final String MINIMAP_FRAME = RavengardFont.glyph(0xE101);
    public static final String PLAYER_MARKER = RavengardFont.glyph(0xE102);
    public static final String TEAMMATE_MARKER = RavengardFont.glyph(0xE103);
    public static final String TEAMMATE_MARKER_ALT = RavengardFont.glyph(0xE106);
    public static final String DEATHWALL_WARNING = RavengardFont.glyph(0xE10E);
    public static final String DEATHWALL = RavengardFont.glyph(0xE10F);

    private RavengardHudGlyphs() {
    }

    public static String hubTiles() {
        return tiles(0xE120);
    }

    public static String cryptTiles() {
        return tiles(0xE110);
    }

    private static String tiles(int origin) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < 6; index++) {
            builder.append(RavengardFont.glyph(origin + index));
        }
        return builder.toString();
    }
}
