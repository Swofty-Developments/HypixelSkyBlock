package net.swofty.type.hub.npcs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.commons.ChatColor;

@Getter
@AllArgsConstructor
public enum ChocolateFactoryRank {
    UNEMPLOYED(0, "Unemployed", ChatColor.RED),
    INTERN(1, "Intern", ChatColor.GRAY),
    EMPLOYEE(20, "Employee", ChatColor.GREEN),
    ASSISTANT(120, "Assistant", ChatColor.BLUE),
    MANAGER(140, "Manager", ChatColor.DARK_PURPLE),
    DIRECTOR(180, "Director", ChatColor.GOLD),
    EXECUTIVE(200, "Executive", ChatColor.LIGHT_PURPLE),
    BOARD_MEMBER(220, "Board Member", ChatColor.AQUA);

    private final int level;
    private final String name;
    private final ChatColor color;

    /**
     * Gets the formatted hologram line for this rank using the rank's base level.
     * Format: §7[Lvl X] §{color}RankName
     * For UNEMPLOYED: §cUnemployed (no level)
     */
    public String getHologramLine() {
        return getHologramLine(level);
    }

    /**
     * Gets the formatted hologram line for this rank with a specific level.
     * Format: §7[Lvl X] §{color}RankName
     * For UNEMPLOYED: §cUnemployed (no level)
     */
    public String getHologramLine(int actualLevel) {
        if (this == UNEMPLOYED) {
            return color + name;
        }
        return ChatColor.GRAY + "[" + actualLevel + "] " + color + name;
    }

    /**
     * Gets the formatted chat name for this rank.
     * Format: §{color}NpcName
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
