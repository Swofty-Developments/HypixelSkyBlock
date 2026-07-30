package net.swofty.type.skyblockgeneric.item.crafting;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Shared source of truth for furnace conversions. Consumers such as minion
 * upgrades use the same recipes instead of maintaining their own item switch.
 */
public final class FurnaceRecipeRegistry {
    private static final int DEFAULT_COOK_TIME = 200;
    private static final Map<ItemType, FurnaceRecipe> RECIPES = new EnumMap<>(ItemType.class);

    static {
        registerVanillaRecipes();
    }

    private FurnaceRecipeRegistry() {
    }

    public static void register(ItemType input, ItemType output, float experience, int cookTime) {
        if (cookTime <= 0) throw new IllegalArgumentException("cookTime must be positive");
        RECIPES.put(input, new FurnaceRecipe(input, output, experience, cookTime));
    }

    public static Optional<FurnaceRecipe> find(ItemType input) {
        return Optional.ofNullable(input == null ? null : RECIPES.get(input));
    }

    public static Optional<SkyBlockItem> smelt(SkyBlockItem input) {
        if (input == null) return Optional.empty();
        return find(input.getAttributeHandler().getPotentialType())
                .map(recipe -> new SkyBlockItem(recipe.output(), input.getAmount()));
    }

    public static Map<ItemType, FurnaceRecipe> recipes() {
        return Collections.unmodifiableMap(RECIPES);
    }

    private static void registerVanillaRecipes() {
        recipe("IRON_ORE", "IRON_INGOT", 0.7f);
        recipe("DEEPSLATE_IRON_ORE", "IRON_INGOT", 0.7f);
        recipe("RAW_IRON", "IRON_INGOT", 0.7f);
        recipe("GOLD_ORE", "GOLD_INGOT", 1.0f);
        recipe("DEEPSLATE_GOLD_ORE", "GOLD_INGOT", 1.0f);
        recipe("NETHER_GOLD_ORE", "GOLD_INGOT", 1.0f);
        recipe("RAW_GOLD", "GOLD_INGOT", 1.0f);
        recipe("COPPER_ORE", "COPPER_INGOT", 0.7f);
        recipe("DEEPSLATE_COPPER_ORE", "COPPER_INGOT", 0.7f);
        recipe("RAW_COPPER", "COPPER_INGOT", 0.7f);
        recipe("ANCIENT_DEBRIS", "NETHERITE_SCRAP", 2.0f);
        recipe("COBBLESTONE", "STONE", 0.1f);
        recipe("STONE", "SMOOTH_STONE", 0.1f);
        recipe("SAND", "GLASS", 0.1f);
        recipe("RED_SAND", "GLASS", 0.1f);
        recipe("CLAY_BALL", "BRICK", 0.3f);
        recipe("CLAY", "TERRACOTTA", 0.35f);
        recipe("NETHERRACK", "NETHER_BRICK", 0.1f);
        recipe("CACTUS", "GREEN_DYE", 1.0f);
        recipe("WET_SPONGE", "SPONGE", 0.15f);
        recipe("POTATO", "BAKED_POTATO", 0.35f);
        recipe("KELP", "DRIED_KELP", 0.1f);
        recipe("CHORUS_FRUIT", "POPPED_CHORUS_FRUIT", 0.1f);
        recipe("BEEF", "COOKED_BEEF", 0.35f);
        recipe("CHICKEN", "COOKED_CHICKEN", 0.35f);
        recipe("PORKCHOP", "COOKED_PORKCHOP", 0.35f);
        recipe("MUTTON", "COOKED_MUTTON", 0.35f);
        recipe("RABBIT", "COOKED_RABBIT", 0.35f);
        recipe("COD", "COOKED_COD", 0.35f);
        recipe("SALMON", "COOKED_SALMON", 0.35f);
        for (String wood : new String[]{"OAK", "SPRUCE", "BIRCH", "JUNGLE", "ACACIA", "DARK_OAK", "MANGROVE", "CHERRY", "PALE_OAK"}) {
            recipe(wood + "_LOG", "CHARCOAL", 0.15f);
            recipe(wood + "_WOOD", "CHARCOAL", 0.15f);
            recipe("STRIPPED_" + wood + "_LOG", "CHARCOAL", 0.15f);
            recipe("STRIPPED_" + wood + "_WOOD", "CHARCOAL", 0.15f);
        }
    }

    private static void recipe(String input, String output, float experience) {
        try {
            register(ItemType.valueOf(input), ItemType.valueOf(output), experience, DEFAULT_COOK_TIME);
        } catch (IllegalArgumentException ignored) {
            // ItemType is generated from configuration; skip recipes for unavailable versions.
        }
    }

    public record FurnaceRecipe(ItemType input, ItemType output, float experience, int cookTime) {
    }
}
