package net.swofty.commons.zombies;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum ZombiesGameType {
    NORMAL(0, "Normal", ZombiesDifficulty.NORMAL),
    RIP(1, "RIP", ZombiesDifficulty.HARD);

    private final int id;
    private final String displayName;
    private final ZombiesDifficulty difficulty;

    ZombiesGameType(int id, String displayName, ZombiesDifficulty difficulty) {
        this.id = id;
        this.displayName = displayName;
        this.difficulty = difficulty;
    }

    public int getMaxPlayers() {
        return 4;
    }

    public int getMinPlayers() {
        return 4;
    }

    @Nullable
    public static ZombiesGameType from(String field) {
        for (ZombiesGameType type : values()) {
            if (type.name().equalsIgnoreCase(field)) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    public static ZombiesGameType fromDisplayName(String displayName) {
        for (ZombiesGameType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    public static ZombiesGameType fromId(int id) {
        for (ZombiesGameType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    public boolean isRIP() {
        return this == RIP;
    }

    @Getter
    public enum ZombiesDifficulty {
        NORMAL("Normal", 1.0),
        HARD("Hard", 1.5);

        private final String displayName;
        private final double difficultyMultiplier;

        ZombiesDifficulty(String displayName, double difficultyMultiplier) {
            this.displayName = displayName;
            this.difficultyMultiplier = difficultyMultiplier;
        }
    }
}
