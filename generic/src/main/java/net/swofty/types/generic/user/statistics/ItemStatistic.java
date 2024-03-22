package net.swofty.types.generic.user.statistics;

import lombok.Getter;

@Getter
public enum ItemStatistic {
    // Non-Player Statistics
    DAMAGE("Damage", true, null, "+", "", "❁"),

    // Player Statistics
    HEALTH("Health", true, "§c", "+", "", "❤"),
    DEFENSE("Defense", false, "§a", "+", "", "❈"),
    SPEED("Speed", false, "§f", "+", "", "✦"),
    STRENGTH("Strength", true, "§c", "+", "", "❁"),
    INTELLIGENCE("Intelligence", false, "§b", "+", "", "✎"),
    MINING_SPEED("Mining Speed", false, "§6", "+", "", "⸕"),
    CRIT_CHANCE("Crit Chance", true, "§b", "+", "%", "☠"),
    CRIT_DAMAGE("Crit Damage", true, "§b", "+", "%", "☣"),
    FEROCITY("Ferocity", false, "§a", "+", "", "⫽"),
    MAGIC_FIND("Magic Find", false, "§d", "+", "%", "✯"),
    //BONUS_ATTACK_SPEED("Bonus Attack Speed", false, "§e", "+", "⚔"),
    ;

    private final String displayName;
    private final boolean isRed;
    private final String colour;
    private final String prefix;
    private final String suffix;
    private final String symbol;

    ItemStatistic(String displayName, boolean isRed, String colour, String prefix, String suffix, String symbol) {
        this.displayName = displayName;
        this.isRed = isRed;
        this.colour = colour;
        this.prefix = prefix;
        this.suffix = suffix;
        this.symbol = symbol;
    }

}
