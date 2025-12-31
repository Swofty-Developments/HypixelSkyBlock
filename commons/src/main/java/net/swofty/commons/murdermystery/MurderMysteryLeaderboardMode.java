package net.swofty.commons.murdermystery;

import lombok.Getter;

@Getter
public enum MurderMysteryLeaderboardMode {
    CLASSIC("classic", "Classic"),
    DOUBLE_UP("double_up", "Double Up!"),
    ASSASSINS("assassins", "Assassins"),
    INFECTION("infection", "Infection");

    private final String key;
    private final String displayName;

    MurderMysteryLeaderboardMode(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public boolean includes(MurderMysteryGameType gameType) {
        return switch (this) {
            case CLASSIC -> gameType == MurderMysteryGameType.CLASSIC;
            case DOUBLE_UP -> gameType == MurderMysteryGameType.DOUBLE_UP;
            case ASSASSINS -> gameType == MurderMysteryGameType.ASSASSINS;
            case INFECTION -> false; // Not implemented yet
        };
    }

    public MurderMysteryLeaderboardMode next() {
        MurderMysteryLeaderboardMode[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public MurderMysteryLeaderboardMode previous() {
        MurderMysteryLeaderboardMode[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static MurderMysteryLeaderboardMode fromKey(String key) {
        for (MurderMysteryLeaderboardMode mode : values()) {
            if (mode.key.equalsIgnoreCase(key)) {
                return mode;
            }
        }
        return CLASSIC;
    }

    public static MurderMysteryLeaderboardMode fromGameType(MurderMysteryGameType gameType) {
        return switch (gameType) {
            case CLASSIC -> CLASSIC;
            case DOUBLE_UP -> DOUBLE_UP;
            case ASSASSINS -> ASSASSINS;
        };
    }
}
