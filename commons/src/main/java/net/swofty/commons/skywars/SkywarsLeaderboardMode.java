package net.swofty.commons.skywars;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum SkywarsLeaderboardMode {
    ALL("all", "All Modes"),
    SOLO_NORMAL("solo_normal", "Solo Normal"),
    SOLO_INSANE("solo_insane", "Solo Insane"),
    DOUBLES("doubles", "Doubles");

    private final String key;
    private final String displayName;

    SkywarsLeaderboardMode(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    private static final List<SkywarsGameType> ACTIVE_MODES = Arrays.asList(
            SkywarsGameType.SOLO_NORMAL,
            SkywarsGameType.SOLO_INSANE,
            SkywarsGameType.DOUBLES_NORMAL
    );

    public boolean includes(SkywarsGameType gameType) {
        return switch (this) {
            case ALL -> ACTIVE_MODES.contains(gameType);
            case SOLO_NORMAL -> gameType == SkywarsGameType.SOLO_NORMAL;
            case SOLO_INSANE -> gameType == SkywarsGameType.SOLO_INSANE;
            case DOUBLES -> gameType == SkywarsGameType.DOUBLES_NORMAL;
        };
    }

    public SkywarsLeaderboardMode next() {
        SkywarsLeaderboardMode[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public SkywarsLeaderboardMode previous() {
        SkywarsLeaderboardMode[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static SkywarsLeaderboardMode fromKey(String key) {
        for (SkywarsLeaderboardMode mode : values()) {
            if (mode.key.equalsIgnoreCase(key)) {
                return mode;
            }
        }
        return ALL;
    }

    public static SkywarsLeaderboardMode fromGameType(SkywarsGameType gameType) {
        return switch (gameType) {
            case SOLO_NORMAL -> SOLO_NORMAL;
            case SOLO_INSANE -> SOLO_INSANE;
            case DOUBLES_NORMAL -> DOUBLES;
            default -> ALL;
        };
    }
}
