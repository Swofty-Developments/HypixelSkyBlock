package net.swofty.packer.packs;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.font.FontProvider;

public enum TestingTexture {
    FULL_SCREEN_BLACK("\uE000", IntendedLocation.TITLE, 200, 600),
    VILLAGER_SPEAK_OUTLINE("\uE001", IntendedLocation.TITLE, -10, 20),
    HUD_BACK_PLATE("\uE002", IntendedLocation.ACTIONBAR, -25, 10),
    ;

    @Getter
    public final String unicode;
    private final IntendedLocation intendedLocation;
    public final int ascent;
    public final int height;

    TestingTexture(String unicode, IntendedLocation intendedLocation, int ascent, int height) {
        this.unicode = unicode;
        this.intendedLocation = intendedLocation;
        this.ascent = ascent;
        this.height = height;
    }

    @Override
    public String toString() {
        return unicode;
    }

    public FontProvider toFontProvider() {
        return FontProvider.bitMap()
                .file(Key.key("hypixel", name().toLowerCase() + ".png"))
                .ascent(ascent)
                .height(height)
                .characters(unicode)
                .build();
    }

    enum IntendedLocation {
        TITLE,
        BOSSBAR,
        ACTIONBAR
    }
}
