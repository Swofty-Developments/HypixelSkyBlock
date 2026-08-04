package net.swofty.type.ravengardgeneric.gui;

import net.kyori.adventure.key.Key;

public final class RavengardFont {
    public static final Key DEFAULT = Key.key("minecraft", "default");
    public static final Key HALF = Key.key("minecraft", "-half");
    public static final Key FULL = Key.key("minecraft", "-full");
    /** Vanilla Illager runes. Unreleased buttons render their whole tooltip in this so it reads as gibberish. */
    public static final Key ILLAGERALT = Key.key("minecraft", "illageralt");

    private static final int SPACE_ORIGIN = 0xD0000;
    private static final int SPACE_MIN = -8192;
    private static final int SPACE_MAX = 8192;

    private RavengardFont() {
    }

    public static String space(int pixels) {
        int clamped = Math.max(SPACE_MIN, Math.min(SPACE_MAX, pixels));
        return new String(Character.toChars(SPACE_ORIGIN + clamped));
    }

    public static String glyph(int codePoint) {
        return new String(Character.toChars(codePoint));
    }
}
