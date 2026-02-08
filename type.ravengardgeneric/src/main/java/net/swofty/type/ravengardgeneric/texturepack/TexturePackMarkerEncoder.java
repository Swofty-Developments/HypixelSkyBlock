package net.swofty.type.ravengardgeneric.texturepack;

import net.minestom.server.map.MapColors;

final class TexturePackMarkerEncoder {
    private static final byte MARKER_ORANGE = MapColors.COLOR_ORANGE.baseColor();
    private static final byte MARKER_MAGENTA = MapColors.COLOR_MAGENTA.baseColor();
    private static final byte MARKER_LIGHT_BLUE = MapColors.COLOR_LIGHT_BLUE.baseColor();
    private static final byte MARKER_YELLOW = MapColors.COLOR_YELLOW.baseColor();
    private static final byte ANCHOR_BIT0_ZERO = MapColors.COLOR_RED.baseColor();
    private static final byte ANCHOR_BIT0_ONE = MapColors.COLOR_BLUE.baseColor();
    private static final byte ANCHOR_BIT1_ZERO = MapColors.COLOR_YELLOW.baseColor();
    private static final byte ANCHOR_BIT1_ONE = MapColors.COLOR_CYAN.baseColor();
    private static final byte ANCHOR_BIT2_ZERO = MapColors.COLOR_GREEN.baseColor();
    private static final byte ANCHOR_BIT2_ONE = MapColors.COLOR_MAGENTA.baseColor();

    void writeMarkerSignature(byte[] colors, TexturePackRenderer.HudAnchor anchor) {
        int mapSize = TexturePackRenderConstants.MAP_SIZE;
        colors[0] = MARKER_ORANGE;
        colors[1] = MARKER_MAGENTA;
        colors[mapSize] = MARKER_LIGHT_BLUE;
        colors[mapSize + 1] = MARKER_YELLOW;
        int value = anchor.code();
        colors[2] = (value & 0b001) != 0 ? ANCHOR_BIT0_ONE : ANCHOR_BIT0_ZERO;
        colors[3] = (value & 0b010) != 0 ? ANCHOR_BIT1_ONE : ANCHOR_BIT1_ZERO;
        colors[4] = (value & 0b100) != 0 ? ANCHOR_BIT2_ONE : ANCHOR_BIT2_ZERO;
    }
}
