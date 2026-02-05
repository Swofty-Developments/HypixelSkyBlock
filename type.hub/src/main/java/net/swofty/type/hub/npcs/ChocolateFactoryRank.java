package net.swofty.type.hub.npcs;

public enum ChocolateFactoryRank {
    UNEMPLOYED(0, "Unemployed", "§c"),
    INTERN(1, "Intern", "§7"),
    EMPLOYEE(20, "Employee", "§a"),
    ASSISTANT(120, "Assistant", "§9"),
    MANAGER(140, "Manager", "§5"),
    DIRECTOR(180, "Director", "§6"),
    EXECUTIVE(200, "Executive", "§d"),
    BOARD_MEMBER(220, "Board Member", "§b");

    private final int level;
    private final String name;
    private final String color;

    ChocolateFactoryRank(int level, String name, String color) {
        this.level = level;
        this.name = name;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    /**
     * Gets the formatted hologram line for this rank.
     * Format: §7[Lvl X] §{color}RankName
     * For UNEMPLOYED: §cUnemployed (no level)
     */
    public String getHologramLine() {
        if (this == UNEMPLOYED) {
            return color + name;
        }
        return "§7[Lvl " + level + "] " + color + name;
    }

    /**
     * Gets the formatted chat name for this rank.
     * Format: §{color}RankName
     */
    public String getChatName(String npcName) {
        return color + npcName;
    }

    /**
     * Gets the rank for a given level.
     * Returns the highest rank the level qualifies for.
     */
    public static ChocolateFactoryRank fromLevel(int level) {
        ChocolateFactoryRank result = UNEMPLOYED;
        for (ChocolateFactoryRank rank : values()) {
            if (level >= rank.level) {
                result = rank;
            }
        }
        return result;
    }
}
