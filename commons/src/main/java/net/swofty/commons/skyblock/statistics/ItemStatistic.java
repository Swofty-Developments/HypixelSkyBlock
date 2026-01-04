package net.swofty.commons.skyblock.statistics;

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
    CRITICAL_CHANCE("Crit Chance", "§c", "§9",
            true, "☣", 30D, 1D),
    CRITICAL_DAMAGE("Crit Damage", "§c", "§9",
            true, "☠", 50D, 1D),
    BONUS_ATTACK_SPEED("Bonus Attack Speed", "§c", "§e", true, "⚔"),
    ABILITY_DAMAGE("Ability Damage", "§c", "§c", true, "๑"),
    TRUE_DEFENSE("True Defense", "§a", "§f", false, "❂"),
    FEROCITY("Ferocity", "§a", "§c", false, "⫽"),
    HEALTH_REGENERATION("Health Regen", "§a", "§c",
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
    MINING_SPREAD("Mining Spread", "§a", "§e", false, "▚"),
    GEMSTONE_SPREAD("Gemstone Spread", "§a", "§e", false, "▚"),
    HUNTER_FORTUNE("Hunter Fortune", "§a", "§d", false, "☘"),
    SWEEP("Sweep", "§a", "§2", false, "∮"),

    // Sub-Gathering Stats
    ORE_FORTUNE("Ore Fortune", "§a", "§6", false, "☘"),
    BLOCK_FORTUNE("Block Fortune", "§a", "§6", false, "☘"),
    DWARVEN_METAL_FORTUNE("Dwarven Metal Fortune", "§a", "§6", false, "☘"),
    GEMSTONE_FORTUNE("Gemstone Fortune", "§a", "§6", false, "☘"),

    WHEAT_FORTUNE("Wheat Fortune", "§a", "§6", false, "☘"),
    POTATO_FORTUNE("Potato Fortune", "§a", "§6", false, "☘"),
    CARROT_FORTUNE("Carrot Fortune", "§a", "§6", false, "☘"),
    PUMPKIN_FORTUNE("Pumpkin Fortune", "§a", "§6", false, "☘"),
    MELON_FORTUNE("Melon Fortune", "§a", "§6", false, "☘"),
    CACTUS_FORTUNE("Cactus Fortune", "§a", "§6", false, "☘"),
    NETHER_WART_FORTUNE("Nether Wart Fortune", "§a", "§6", false, "☘"),
    COCOA_BEANS_FORTUNE("Cocoa Beans Fortune", "§a", "§6", false, "☘"),
    MUSHROOM_FORTUNE("Mushroom Fortune", "§a", "§6", false, "☘"),
    SUGAR_CANE_FORTUNE("Sugar Cane Fortune", "§a", "§6", false, "☘"),

    FIG_FORTUNE("Fig Fortune", "§a", "§6", false, "☘"),
    MANGROVE_FORTUNE("Mangrove Fortune", "§a", "§6", false, "☘"),

    // Wisdom Stats
    ALCHEMY_WISDOM("Alchemy Wisdom", "§a", "§3", false, "☯"),
    CARPENTRY_WISDOM("Carpentry Wisdom", "§a", "§3", false, "☯"),
    COMBAT_WISDOM("Combat Wisdom", "§a", "§3", false, "☯"),
    ENCHANTING_WISDOM("Enchanting Wisdom", "§a", "§3", false, "☯"),
    FARMING_WISDOM("Farming Wisdom", "§a", "§3", false, "☯"),
    FISHING_WISDOM("Fishing Wisdom", "§a", "§3", false, "☯"),
    FORAGING_WISDOM("Foraging Wisdom", "§a", "§3", false, "☯"),
    MINING_WISDOM("Mining Wisdom", "§a", "§3", false, "☯"),
    RUNE_CRAFTING_WISDOM("Runecrafting Wisdom", "§a", "§3", false, "☯"),
    SOCIAL_WISDOM("Social Wisdom", "§a", "§3", false, "☯"),
    TAMING_WISDOM("Taming Wisdom", "§a", "§3", false, "☯"),
    HUNTING_WISDOM("Hunting Wisdom", "§a", "§3", false, "☯"),

    // Misc Stats
    SPEED("Speed", "§a", "§f", false, "✦", 100D, 1D),
    MAGIC_FIND("Magic Find", "§a", "§b", false, "✯"),
    PET_LUCK("Pet Luck", "§a", "§d", false, "♣"),
    SEA_CREATURE_CHANCE("Sea Creature Chance", "§c", "§9", true, "α", 2D, 1D),
    FISHING_SPEED("Fishing Speed", "§a", "§b", false, "☂"),
    BONUS_PEST_CHANCE("Bonus Pest Chance", "§a", "§2", true, "ൠ"),
    HEAT_RESISTANCE("Heat Resistance","§a","§c",false,"♨"),
    COLD_RESISTANCE("Cold Resistance", "§a", "§b", false, "❄"),
    FEAR("Fear","§a","§5",false,"☠"),
    PULL("Pull","§a","§b",false,"ᛷ"),
    RESPIRATION("Respiration","§a","§3",false,"⚶"),
    PRESSURE_RESISTANCE("Pressure Resistance","§a","§9",false,"❍"),
    BREWER("Brewer", "§a", "§d", true, "☕"),

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

    public String getFullDisplayName() {
        return displayColor + symbol + " " + displayName;
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
