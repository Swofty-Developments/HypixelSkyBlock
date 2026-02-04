package net.swofty.commons.zombies;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ZombiesLeaderboardMode {
    ALL("all", "All Maps"),
    DEAD_END("dead_end", "Dead End"),
    BAD_BLOOD("bad_blood", "Bad Blood"),
    ALIEN_ARCADIUM("alien_arcadium", "Alien Arcadium"),
    PRISON("prison", "Prison");

    private final String key;
    private final String displayName;

    ZombiesLeaderboardMode(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    private static final List<ZombiesMap> ACTIVE_MAPS = Arrays.asList(
            ZombiesMap.DEAD_END,
            ZombiesMap.BAD_BLOOD,
            ZombiesMap.ALIEN_ARCADIUM,
            ZombiesMap.PRISON
    );

    public boolean includes(ZombiesMap map) {
        return switch (this) {
            case ALL -> ACTIVE_MAPS.contains(map);
            case DEAD_END -> map == ZombiesMap.DEAD_END;
            case BAD_BLOOD -> map == ZombiesMap.BAD_BLOOD;
            case ALIEN_ARCADIUM -> map == ZombiesMap.ALIEN_ARCADIUM;
            case PRISON -> map == ZombiesMap.PRISON;
        };
    }

    public ZombiesLeaderboardMode next() {
        ZombiesLeaderboardMode[] values = values();
        int nextOrdinal = (this.ordinal() + 1) % values.length;
        return values[nextOrdinal];
    }

    public ZombiesLeaderboardMode previous() {
        ZombiesLeaderboardMode[] values = values();
        int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
        return values[prevOrdinal];
    }

    public static ZombiesLeaderboardMode fromKey(String key) {
        for (ZombiesLeaderboardMode mode : values()) {
            if (mode.key.equalsIgnoreCase(key)) {
                return mode;
            }
        }
        return ALL;
    }

    public static ZombiesLeaderboardMode fromMap(ZombiesMap map) {
        return switch (map) {
            case DEAD_END -> DEAD_END;
            case BAD_BLOOD -> BAD_BLOOD;
            case ALIEN_ARCADIUM -> ALIEN_ARCADIUM;
            case PRISON -> PRISON;
        };
    }
}
