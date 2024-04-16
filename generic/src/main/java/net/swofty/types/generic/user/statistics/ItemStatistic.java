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

    // Combat Statistics
    HEALTH("Health", false, "§c", "+", "", "❤"),
    DEFENSE("Defense", false, "§a", "+", "", "❈"),
    STRENGTH("Strength", true, "§c", "+", "", "❁"),
    INTELLIGENCE("Intelligence", false, "§b", "+", "", "✎"),
    CRIT_CHANCE("Crit Chance", true, "§9", "+", "%", "☠"),
    CRIT_DAMAGE("Crit Damage", true, "§9", "+", "%", "☣"),
    FEROCITY("Ferocity", false, "§a", "+", "", "⫽"),
    BONUS_ATTACK_SPEED("Bonus Attack Speed", false, "§e", "+", "", "⚔"),
    ABILITY_DAMAGE("Ability Damage", false, "§c", "+", "", "๑"),
    HEALTH_REGEN("Health Regen", false, "§c", "+", "", "❣"),
    TRUE_DEFENSE("True Defense", false, "§f", "+", "", "❂"),

    // Misc Statistics
    SPEED("Speed", false, "§f", "+", "%", "✦"),
    MAGIC_FIND("Magic Find", false, "§b", "+", "", "✯"),
    SEA_CREATURE_CHANCE("Sea Creature Chance", false, "§3", "+", "%", "α"),
    PET_LUCK("Pet Luck", false, "§d", "+", "", "☘"),

    // Wisdom Statistics
    COMBAT_WISDOM("Combat Wisdom", false, "§3", "+", "", "☯"),
    MINING_WISDOM("Mining Wisdom", false, "§3", "+", "", "☯"),
    FARMING_WISDOM("Farming Wisdom", false, "§3", "+", "", "☯"),
    FORAGING_WISDOM("Foraging Wisdom", false, "§3", "+", "", "☯"),
    FISHING_WISDOM("Fishing Wisdom", false, "§3", "+", "", "☯"),
    ENCHANTING_WISDOM("Enchanting Wisdom", false, "§3", "+", "", "☯"),
    ALCHEMY_WISDOM("Alchemy Wisdom", false, "§3", "+", "", "☯"),
    CARPENTRY_WISDOM("Carpentry Wisdom", false, "§3", "+", "", "☯"),
    SOCIAL_WISDOM("Social Wisdom", false, "§3", "+", "", "☯"),
    TAMING_WISDOM("Taming Wisdom", false, "§3", "+", "", "☯"),
    RUNECRAFTING_WISDOM("Runecrafting Wisdom", false, "§3", "+", "", "☯"),

    // Gathering Statistics
    MINING_SPEED("Mining Speed", false, "§6", "+", "", "⸕"),
    FARMING_FORTUNE("Farming Fortune", false, "§6", "+", "", "☘"),
    FORAGING_FORTUNE("Foraging Fortune", false, "§6", "+", "", "☘"),
    MINING_FORTUNE("Mining Fortune", false, "§6", "+", "", "☘"),
    BREAKING_POWER("Breaking Power", false, "§2", "+", "", "Ⓟ"),
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
