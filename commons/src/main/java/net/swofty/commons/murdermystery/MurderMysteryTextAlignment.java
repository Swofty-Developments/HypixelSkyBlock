package net.swofty.commons.murdermystery;

import lombok.Getter;

@Getter
public enum MurderMysteryTextAlignment {
    CENTER("center", "Center"),
    BLOCK("block", "Block");

    private final String key;
    private final String displayName;

    MurderMysteryTextAlignment(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public MurderMysteryTextAlignment next() {
        MurderMysteryTextAlignment[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public MurderMysteryTextAlignment previous() {
        MurderMysteryTextAlignment[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static MurderMysteryTextAlignment fromKey(String key) {
        for (MurderMysteryTextAlignment alignment : values()) {
            if (alignment.key.equalsIgnoreCase(key)) {
                return alignment;
            }
        }
        return CENTER;
    }
}
