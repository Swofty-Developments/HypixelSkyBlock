package net.swofty.types.generic.item.impl;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Getter
public abstract class SkyBlockRecipe<T> {
    protected SkyBlockItem result;
    @Setter
    protected Function<SkyBlockPlayer, CraftingResult> canCraft;
    @Setter
    protected int amount = 1;
    @Getter
    protected RecipeType recipeType;

    protected SkyBlockRecipe(SkyBlockItem result, RecipeType recipeType, Function<SkyBlockPlayer, CraftingResult> canCraft) {
        this.result = result;
        this.recipeType = recipeType;
        this.canCraft = canCraft;
    }

    public abstract T setResult(SkyBlockItem result);

    public abstract void init();

    public abstract SkyBlockItem[] consume(SkyBlockItem[] stacks);

    public abstract SkyBlockItem[] getRecipeDisplay();

    public abstract SkyBlockRecipe clone();

    public static SkyBlockRecipe<?> parseRecipe(ItemStack[] stacks) {
        ShapedRecipe shapedRecipe = ShapedRecipe.parseShapedRecipe(stacks);
        if (shapedRecipe != null) {
            return shapedRecipe;
        }
        return ShapelessRecipe.parseShapelessRecipe(stacks);
    }

    public static List<String> getMissionDisplay(List<String> lore, UUID uuid) {
        ArrayList<SkyBlockRecipe> allRecipes = new ArrayList<>();
        ArrayList<SkyBlockRecipe> allowedRecipes = new ArrayList<>();
        allRecipes.addAll(ShapedRecipe.CACHED_RECIPES);
        allRecipes.addAll(ShapelessRecipe.CACHED_RECIPES);

        allRecipes.forEach(recipe -> {
            SkyBlockRecipe.CraftingResult result =
                    (SkyBlockRecipe.CraftingResult) recipe.getCanCraft().apply(SkyBlockGenericLoader.getFromUUID(uuid));

            if (result.allowed()) {
                allowedRecipes.add(recipe);
            }
        });

        String unlockedPercentage = String.format("%.2f", (allowedRecipes.size() / (double) allRecipes.size()) * 100);
        lore.add("§7Recipe Book Unlocked: §e" + unlockedPercentage + "§6%");

        String baseLoadingBar = "─────────────────";
        int maxBarLength = baseLoadingBar.length();
        int completedLength = (int) ((allowedRecipes.size() / (double) allRecipes.size()) * maxBarLength);

        String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
        int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
        String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                maxBarLength
        ));

        lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + allowedRecipes.size() + "§6/§e" + allRecipes.size());

        return lore;
    }

    @Getter
    public enum RecipeType {
        FARMING(Material.GOLDEN_HOE),
        MINING(Material.STONE_PICKAXE),
        COMBAT(Material.STONE_SWORD),
        FISHING(Material.FISHING_ROD),
        FORAGING(Material.JUNGLE_SAPLING),
        ENCHANTING(Material.ENCHANTING_TABLE),
        ALCHEMY(Material.BREWING_STAND),
        CARPENTRY(Material.CRAFTING_TABLE),
        SPECIAL(Material.NETHER_STAR),
        NONE(Material.AIR),
        ;

        private final Material material;

        RecipeType(Material material) {
            this.material = material;
        }
    }

    public record CraftingResult(boolean allowed, String[] errorMessage) {
    }
}
