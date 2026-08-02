package net.swofty.type.ravengardgeneric.item;

import lombok.Getter;

/** Item rarities, each with its pack tag glyph and tooltip style. */
@Getter
public enum RavengardRarity {
    COMMON(0xE203, 0xFFFFFF),
    UNCOMMON(0xE21C, 0x30AC35),
    RARE(0xE218, 0x5555FF),
    EPIC(0xE208, 0xAA00AA),
    LEGENDARY(0xE211, 0xFFAA00);

    private final int tagGlyph;
    private final int nameColor;

    RavengardRarity(int tagGlyph, int nameColor) {
        this.tagGlyph = tagGlyph;
        this.nameColor = nameColor;
    }

    public String tooltipStyle() {
        return "hypixel_ravengard:" + name().toLowerCase();
    }

    public static RavengardRarity fromKey(String key) {
        if (key == null) {
            return COMMON;
        }
        for (RavengardRarity value : values()) {
            if (value.name().equalsIgnoreCase(key)) {
                return value;
            }
        }
        return COMMON;
    }
}
