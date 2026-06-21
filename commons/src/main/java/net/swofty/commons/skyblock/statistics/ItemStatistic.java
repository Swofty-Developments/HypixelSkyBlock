package net.swofty.commons.skyblock.statistics;

import lombok.Getter;
import lombok.NonNull;
import net.minestom.server.item.Material;
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
    SUNFLOWER_FORTUNE("Sunflower Fortune", "§a", "§6", false, "☘"),
    MOONFLOWER_FORTUNE("Moonflower Fortune", "§a", "§6", false, "☘"),
    WILD_ROSE_FORTUNE("Wild Rose Fortune", "§a", "§6", false, "☘"),

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
    TREASURE_CHANCE("Treasure Chance", "§a", "§6", true, "⛃"),
    TROPHY_FISH_CHANCE("Trophy Fish Chance", "§a", "§6", true, "♔"),
    DOUBLE_HOOK_CHANCE("Double Hook Chance", "§a", "§3", true, "⚓"),
    BONUS_PEST_CHANCE("Bonus Pest Chance", "§a", "§2", true, "ൠ"),
    OVERBLOOM("Overbloom", "§a", "§e", false, "☀"),
    HEAT_RESISTANCE("Heat Resistance","§a","§c",false,"♨"),
    COLD_RESISTANCE("Cold Resistance", "§a", "§b", false, "❄"),
    FEAR("Fear","§a","§5",false,"☠"),
    PULL("Pull","§a","§b",false,"ᛷ"),
    RESPIRATION("Respiration","§a","§3",false,"⚶"),
    PRESSURE_RESISTANCE("Pressure Resistance","§a","§9",false,"❍"),
    TRACKING("Tracking", "§a", "§d", false, "❃"),
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

    // it's a method cuz it's in common, so it doesn't require minestom to be imported.
    // or that's how I think that works
    public Material getIconMaterial() {
        return switch (this) {
            case HEALTH -> Material.GOLDEN_APPLE;
            case DEFENSE -> Material.IRON_CHESTPLATE;
            case STRENGTH -> Material.BLAZE_POWDER;
            case INTELLIGENCE -> Material.ENCHANTED_BOOK;
            case BONUS_ATTACK_SPEED -> Material.GOLDEN_AXE;
            case ABILITY_DAMAGE -> Material.BEACON;
            case TRUE_DEFENSE -> Material.BONE_MEAL;
            case FEROCITY -> Material.RED_DYE;
            case HEALTH_REGENERATION -> Material.POTION;
            case VITALITY -> Material.GLISTERING_MELON_SLICE;
            case MENDING -> Material.GHAST_TEAR;
            case SWING_RANGE -> Material.STONE_SWORD;
            case BREAKING_POWER -> Material.EMERALD;
            case MINING_SPEED -> Material.DIAMOND_PICKAXE;
            case MINING_SPREAD, GEMSTONE_SPREAD -> Material.GOLDEN_PICKAXE;
            case SWEEP -> Material.DIAMOND_AXE;
            case WHEAT_FORTUNE -> Material.WHEAT;
            case CARROT_FORTUNE -> Material.CARROT;
            case POTATO_FORTUNE -> Material.POTATO;
            case PUMPKIN_FORTUNE -> Material.CARVED_PUMPKIN;
            case MELON_FORTUNE -> Material.MELON_SLICE;
            case MUSHROOM_FORTUNE -> Material.RED_MUSHROOM;
            case CACTUS_FORTUNE -> Material.CACTUS;
            case SUGAR_CANE_FORTUNE -> Material.SUGAR_CANE;
            case NETHER_WART_FORTUNE -> Material.NETHER_WART;
            case COCOA_BEANS_FORTUNE -> Material.COCOA_BEANS;
            case SUNFLOWER_FORTUNE -> Material.SUNFLOWER;
            case MOONFLOWER_FORTUNE -> Material.BLUE_ORCHID;
            case WILD_ROSE_FORTUNE -> Material.ROSE_BUSH;
            case FIG_FORTUNE, MANGROVE_FORTUNE -> Material.PAPER;
            case HUNTER_FORTUNE -> Material.PRISMARINE_SHARD;
            case PULL -> Material.COBWEB;
            case FISHING_SPEED -> Material.FISHING_ROD;
            case SEA_CREATURE_CHANCE -> Material.PRISMARINE_CRYSTALS;
            case DOUBLE_HOOK_CHANCE -> Material.COOKED_COD;
            case TREASURE_CHANCE -> Material.CHEST;
            case BONUS_PEST_CHANCE -> Material.DIRT;
            case OVERBLOOM -> Material.CACTUS_FLOWER;
            case SPEED -> Material.SUGAR;
            case MAGIC_FIND -> Material.STICK;
            case HEAT_RESISTANCE -> Material.LAVA_BUCKET;
            case COLD_RESISTANCE -> Material.ICE;
            case FEAR -> Material.CAULDRON;
            case RESPIRATION, PRESSURE_RESISTANCE -> Material.GLASS_BOTTLE;
            case TRACKING -> Material.COMPASS;
            case ALCHEMY_WISDOM, CARPENTRY_WISDOM, COMBAT_WISDOM, ENCHANTING_WISDOM,
                 FARMING_WISDOM, FISHING_WISDOM, FORAGING_WISDOM, MINING_WISDOM,
                 RUNE_CRAFTING_WISDOM, SOCIAL_WISDOM, TAMING_WISDOM, HUNTING_WISDOM -> Material.WRITABLE_BOOK;
            default -> Material.PAPER;
        };
    }

    // same case here than above
    public String getIconTexture() {
        return switch (this) {
            case CRITICAL_CHANCE -> "3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b";
            case CRITICAL_DAMAGE -> "ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c";
            case PRISTINE -> "d886e0f41185b18a3afd89488d2ee4caa0735009247cccf039ced6aed752ff1a";
            case MINING_FORTUNE, ORE_FORTUNE, BLOCK_FORTUNE, DWARVEN_METAL_FORTUNE, GEMSTONE_FORTUNE ->
                "b73579575ca88b3a8afe1ed18907b3125fe0987b02a88ef0e8a01087c3d024c4";
            case FORAGING_FORTUNE -> "4e44e2a8dff90f5b005e76e6f5db7c12ae59cbbc56d8bc8050f3e3dbf0c3b734";
            case FARMING_FORTUNE -> "220ee7741ff1b958dbb9fa7cddad9c3cce93373f470f9b834da02da67c8202a4";
            case TROPHY_FISH_CHANCE -> "afe7bfb403d9c8c0cdc539c147035869a23e8810a0f3c74a767140180abd00a7";
            case PET_LUCK -> "bc78314255d8864a753fe95622564046f0dee2a82c6e4e2e7f452fcb95af318c";
            default -> null;
        };
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
