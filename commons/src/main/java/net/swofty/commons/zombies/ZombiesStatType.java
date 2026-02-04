package net.swofty.commons.zombies;

import lombok.Getter;

@Getter
public enum ZombiesStatType {
    ROUNDS_SURVIVED("rounds_survived", "Rounds Survived", false),
    WINS("wins", "Wins", false),
    BEST_ROUND("best_round", "Best Round", false),
    KILLS("kills", "Kills", false),
    PLAYERS_REVIVED("players_revived", "Players Revived", false),
    DOORS_OPENED("doors_opened", "Doors Opened", false),
    WINDOWS_REPAIRED("windows_repaired", "Windows Repaired", false),
    TIMES_KNOCKED_DOWN("times_knocked_down", "Times Knocked Down", false),
    DEATHS("deaths", "Deaths", false);

    private final String key;
    private final String displayName;
    private final boolean timedTracking;

    ZombiesStatType(String key, String displayName, boolean timedTracking) {
        this.key = key;
        this.displayName = displayName;
        this.timedTracking = timedTracking;
    }

    public static ZombiesStatType fromKey(String key) {
        for (ZombiesStatType type : values()) {
            if (type.key.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return WINS;
    }
}
