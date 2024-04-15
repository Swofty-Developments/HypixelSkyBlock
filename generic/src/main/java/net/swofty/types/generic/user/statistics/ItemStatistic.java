package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

@Getter
public enum ItemStatistic {
    // Non-Player Statistics
    DAMAGE("Damage", true, "§c", "+", "", "❁"),
    DAMAGE_ADDITIVE("Damage", true, "§c", "+", "%", "❁"), // EXPECTS PERCENTAGES
    DAMAGE_MULTIPLICATIVE("Damage", true, "§c", "+", "x", "❁"),

    // Player Statistics
    HEALTH("Health", false, "§a", "+", "", "❤"),
    DEFENSE("Defense", false, "§a", "+", "", "❈"),
    SPEED("Speed", false, "§f", "+", "", "✦"),
    STRENGTH("Strength", true, "§c", "+", "", "❁"),
    INTELLIGENCE("Intelligence", false, "§b", "+", "", "✎"),
    MINING_SPEED("Mining Speed", false, "§6", "+", "", "⸕"),
    CRIT_CHANCE("Crit Chance", true, "§9", "+", "%", "☠"),
    CRIT_DAMAGE("Crit Damage", true, "§9", "+", "%", "☣"),
    FEROCITY("Ferocity", false, "§a", "+", "", "⫽"),
    MAGIC_FIND("Magic Find", false, "§b", "+", "%", "✯"),
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

    public static String getStatisticDisplayFloat(SkyBlockPlayer player, ItemStatistic statistic) {
        PlayerStatistics statistics = player.getStatistics();
        return  " " + statistic.getColour() + statistic.getSymbol() + " " + StringUtility.toNormalCase((statistic.name()) + " §f" + statistics.allStatistics().get(statistic).floatValue()) + statistic.getSuffix();
    }
    public static String getStatisticDisplayInt(SkyBlockPlayer player, ItemStatistic statistic) {
        PlayerStatistics statistics = player.getStatistics();
        return  " " + statistic.getColour() + statistic.getSymbol() + " " + StringUtility.toNormalCase((statistic.name()) + " §f" + statistics.allStatistics().get(statistic).intValue()) + statistic.getSuffix();
    }

}
