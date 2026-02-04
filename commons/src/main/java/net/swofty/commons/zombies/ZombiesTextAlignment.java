package net.swofty.commons.zombies;

import lombok.Getter;

@Getter
public enum ZombiesTextAlignment {
    CENTER("center", "Center"),
    BLOCK("block", "Block");

    private final String key;
    private final String displayName;

    ZombiesTextAlignment(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public ZombiesTextAlignment next() {
        ZombiesTextAlignment[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public ZombiesTextAlignment previous() {
        ZombiesTextAlignment[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static ZombiesTextAlignment fromKey(String key) {
        for (ZombiesTextAlignment alignment : values()) {
            if (alignment.key.equalsIgnoreCase(key)) {
                return alignment;
            }
        }
        return CENTER;
    }
}
