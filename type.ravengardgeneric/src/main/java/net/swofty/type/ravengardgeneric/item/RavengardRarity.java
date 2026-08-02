package net.swofty.type.ravengardgeneric.item;

import lombok.Getter;

/** Item rarities, each with its pack tag glyph and tooltip style. */
@Getter
public enum RavengardRarity {
    COMMON(0xE203),
    UNCOMMON(0xE21C),
    RARE(0xE218),
    EPIC(0xE208),
    LEGENDARY(0xE211);

    private final int tagGlyph;

    RavengardRarity(int tagGlyph) {
        this.tagGlyph = tagGlyph;
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
