package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Getter
public enum ItemStatistic {
    // Combat Stats
    HEALTH("Health", "§a", "§c",
            false, "❤", 100D, 1D),
    DEFENSE("Defense", "§a", "§a", false, "❈"),
    STRENGTH("Strength", "§c", "§c", false, "❁"),
    INTELLIGENCE("Intelligence", "§a", "§b", false, "✎"),
    CRIT_CHANCE("Crit Chance", "§c", "§9",
            true, "☣", 30D, 1D),
    CRIT_DAMAGE("Crit Damage", "§c", "§9",
            true, "☠", 50D, 1D),
    BONUS_ATTACK_SPEED("Bonus Attack Speed", "§c", "§e", true, "⚔"),
    ABILITY_DAMAGE("Ability Damage", "§c", "§c", true, "๑"),
    TRUE_DEFENSE("True Defense", "§a", "§f", false, "❂"),
    FEROCITY("Ferocity", "§a", "§c", false, "⫽"),
    HEALTH_REGEN("Health Regen", "§a", "§c",
            false, "❣", 100D, 1D),
    VITALITY("Vitality", "§a", "§4",
            false, "♨", 100D, 1D),
    MENDING("Mending", "§a", "§a", false, "☄",
            100D, 1D),
    SWING_RANGE("Swing Range", "§e", "§e", false, "Ⓢ", 3D, 1D),

    // Gathering Stats
    MINING_SPEED("Mining Speed", "§a", "§6", false, "⸕"),
    MINING_FORTUNE("Mining Fortune", "§a", "§6", false, "☘"),
    FARMING_FORTUNE("Farming Fortune", "§a", "§6", false, "☘"),
    FORAGING_FORTUNE("Foraging Fortune", "§a", "§6", false, "☘"),
    BREAKING_POWER("Breaking Power", "§a", "§2", false, "Ⓟ"),
    PRISTINE("Pristine", "§a", "§5", false, "✧"),

    // Misc Stats
    SPEED("Speed", "§a", "§f", false, "✦", 100D, 1D),
    MAGIC_FIND("Magic Find", "§a", "§b", false, "✯"),
    PET_LUCK("Pet Luck", "§a", "§d", false, "♣"),
    SEA_CREATURE_CHANCE("Sea Creature Chance", "§c", "§9", true, "α", 2D, 1D),
    FISHING_SPEED("Fishing Speed", "§a", "§b", false, "☂"),
    COLD_RESISTANCE("Cold Resistance", "§a", "§b", false, "❄"),
    BONUS_PEST_CHANCE("Bonus Pest Chance", "§a", "§2", true, "ൠ"),

    // Other Stats
    DAMAGE("Damage", "§c", "§c", false, "❁",
            5D, 1D),
    ;

    private final @NonNull String displayName;
    private final @NonNull String loreColor;
    private final @NonNull String displayColor;
    private final @NonNull Boolean isPercentage;
    private final @NonNull String symbol;
    private Double baseAdditiveValue = 0D;
    private Double baseMultiplicativeValue = 1D;

    ItemStatistic(@NotNull String displayName, @NotNull String loreColor, @NotNull String displayColor,
                   @NonNull Boolean isPercentage, @NotNull String symbol, @NotNull Double baseAdditiveValue,
                  @NotNull Double baseMultiplicativeValue) {
        this.displayName = displayName;
        this.loreColor = loreColor;
        this.displayColor = displayColor;
        this.isPercentage = isPercentage;
        this.symbol = symbol;
        this.baseAdditiveValue = baseAdditiveValue;
        this.baseMultiplicativeValue = baseMultiplicativeValue;
    }

    ItemStatistic(@NotNull String displayName, @NotNull String loreColor, @NotNull String displayColor,
                  @NonNull Boolean isPercentage, @NotNull String symbol) {
        this.displayName = displayName;
        this.loreColor = loreColor;
        this.displayColor = displayColor;
        this.isPercentage = isPercentage;
        this.symbol = symbol;
    }

    public String getSuffix() {
        return isPercentage ? "%" : "";
    }

    public String getPrefix() {
        return isPercentage ? "" : "+";
    }



    public static ItemStatistics getOfAllBaseValues() {
        ItemStatistics.Builder builder = ItemStatistics.builder();
        for (ItemStatistic stat : ItemStatistic.values()) {
            builder.withBase(stat, stat.baseAdditiveValue);
            builder.withMultiplicative(stat, stat.baseMultiplicativeValue);
        }
        return builder.build();
    }
}
