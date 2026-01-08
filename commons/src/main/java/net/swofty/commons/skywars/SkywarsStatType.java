package net.swofty.commons.skywars;

import lombok.Getter;

@Getter
public enum SkywarsStatType {
    LEVEL("level", "SkyWars Level", false),
    WINS("wins", "Wins", true),
    KILLS("kills", "Kills", true);

    private final String key;
    private final String displayName;
    private final boolean timedTracking;

    SkywarsStatType(String key, String displayName, boolean timedTracking) {
        this.key = key;
        this.displayName = displayName;
        this.timedTracking = timedTracking;
    }

    public static SkywarsStatType fromKey(String key) {
        for (SkywarsStatType type : values()) {
            if (type.key.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return WINS;
    }
}
