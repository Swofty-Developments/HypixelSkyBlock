package net.swofty.commons.skywars;

import lombok.Getter;

@Getter
public enum SkywarsTextAlignment {
    CENTER("center", "Center"),
    BLOCK("block", "Block");

    private final String key;
    private final String displayName;

    SkywarsTextAlignment(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public SkywarsTextAlignment next() {
        SkywarsTextAlignment[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public SkywarsTextAlignment previous() {
        SkywarsTextAlignment[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static SkywarsTextAlignment fromKey(String key) {
        for (SkywarsTextAlignment alignment : values()) {
            if (alignment.key.equalsIgnoreCase(key)) {
                return alignment;
            }
        }
        return CENTER;
    }
}
