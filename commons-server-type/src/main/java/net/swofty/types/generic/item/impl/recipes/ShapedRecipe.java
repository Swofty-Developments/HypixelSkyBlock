package net.swofty.types.generic.item.impl.recipes;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

import java.util.*;
import java.util.function.Function;

@Getter
public class ShapedRecipe extends SkyBlockRecipe<ShapedRecipe> {
    public static final List<ShapedRecipe> CACHED_RECIPES = new ArrayList<>();

    private final Map<Character, MaterialQuantifiable> ingredientMap;
    private final List<String> pattern; // Using a list of strings for simplicity

    public ShapedRecipe(RecipeType type,
                        SkyBlockItem result, Map<Character, MaterialQuantifiable> ingredientMap,
                        List<String> pattern, Function<SkyBlockPlayer, CraftingResult> canCraft) {
        super(result, type, canCraft);
        this.ingredientMap = ingredientMap;
        this.pattern = pattern;
    }

    public ShapedRecipe(RecipeType type,
                        SkyBlockItem result, Map<Character, MaterialQuantifiable> ingredientMap,
                        List<String> pattern) {
        this(type, result, ingredientMap, pattern, (player) -> new CraftingResult(true, new String[]{}));
    }


    @Override
    public ShapedRecipe setResult(SkyBlockItem result) {
        this.result = result;
        return this;
    }

    @Override
    public void init() {
        CACHED_RECIPES.add(this);
    }

    @Override
    public SkyBlockItem[] consume(SkyBlockItem[] stacks) {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>(this.ingredientMap);
        // Remove AIR from the ingredient map
        ingredientMap.remove(' ');

        Map<Character, MaterialQuantifiable> materialsToConsume = new HashMap<>(ingredientMap);
        SkyBlockItem[] modifiedStacks = Arrays.copyOf(stacks, stacks.length);

        int patternRows = pattern.size();
        int patternCols = pattern.get(0).length();

        // Try all possible starting positions (top-left corners) for the pattern in the grid
        for (int startRow = 0; startRow <= 3 - patternRows; startRow++) {
            for (int startCol = 0; startCol <= 3 - patternCols; startCol++) {

                // Iterate through stacks within the potentially shifted pattern
                for (int i = 0; i < modifiedStacks.length; i++) {
                    int gridRow = i / 3;
                    int gridCol = i % 3;

                    // If this stack is within our shifted pattern on the grid
                    if (gridRow >= startRow && gridRow < startRow + patternRows &&
                            gridCol >= startCol && gridCol < startCol + patternCols) {

                        char patternChar = pattern.get(gridRow - startRow).charAt(gridCol - startCol);
                        MaterialQuantifiable patternMaterial = ingredientMap.get(patternChar);

                        if (patternMaterial != null && !patternMaterial.getMaterial().equals(ItemType.AIR)) {
                            MaterialQuantifiable stackMaterial = MaterialQuantifiable.of(modifiedStacks[i].getItemStack());

                            // skip the iteration if stackMaterial is AIR
                            if (stackMaterial.getMaterial().equals(ItemType.AIR)) {
                                continue;
                            }

                            if (stackMaterial.matches(patternMaterial.getMaterial())
                                    || ExchangeableType.isExchangeable(stackMaterial.getMaterial(), patternMaterial.getMaterial())) {
                                int stackAmount = stackMaterial.getAmount();
                                int consumeAmount = patternMaterial.getAmount();

                                if (stackAmount >= consumeAmount) {
                                    stackMaterial.setAmount(stackAmount - consumeAmount);
                                    materialsToConsume.remove(patternChar);

                                    SkyBlockItem item = new SkyBlockItem(stackMaterial.getMaterial());
                                    item.setAmount(stackAmount - consumeAmount);

                                    modifiedStacks[i] = stackMaterial.getAmount() > 0 ? item : null;
                                } else {
                                    throw new IllegalStateException("Not enough materials to consume!");  // We need exact amount for shaped recipes
                                }
                            }
                        }
                    }
                }

                // If all of the materials were consumed, return the modified stacks
                if (materialsToConsume.isEmpty()) {
                    return modifiedStacks;
                }

                // Reset before trying the next position
                materialsToConsume = new HashMap<>(ingredientMap);
                modifiedStacks = Arrays.copyOf(stacks, stacks.length);
            }
        }

        // If there are still materials left to consume, there were not enough materials in the stacks
        throw new IllegalStateException("Not enough materials to consume!");
    }

    @Override
    public SkyBlockItem[] getRecipeDisplay() {
        SkyBlockItem[] recipeDisplay = new SkyBlockItem[9];
        int patternRows = pattern.size();
        int patternCols = pattern.get(0).length();

        for (int row = 0; row < patternRows; row++) {
            for (int col = 0; col < patternCols; col++) {
                char patternChar = pattern.get(row).charAt(col);
                MaterialQuantifiable patternMaterial = ingredientMap.get(patternChar);

                if (patternMaterial != null) {
                    recipeDisplay[row * 3 + col] = new SkyBlockItem(patternMaterial.getMaterial(), patternMaterial.getAmount());
                }
            }
        }

        return recipeDisplay;
    }

    @Override
    public SkyBlockRecipe clone() {
        return new ShapedRecipe(recipeType, result, ingredientMap, pattern, canCraft);
    }

    public static ShapedRecipe parseShapedRecipe(ItemStack[] stacks) {
        ItemStack[][] grid = {
                {stacks[0], stacks[1], stacks[2]},
                {stacks[3], stacks[4], stacks[5]},
                {stacks[6], stacks[7], stacks[8]}
        };

        return CACHED_RECIPES.stream()
                .filter(recipe -> {
                    List<String> recipePattern = recipe.getPattern();
                    int patternRows = recipePattern.size();
                    int patternCols = recipePattern.get(0).length();

                    for (int row = 0; row <= 3 - patternRows; row++) {
                        for (int col = 0; col <= 3 - patternCols; col++) {
                            if (matchesPattern(recipe, grid, row, col)) {
                                return true;
                            }
                        }
                    }

                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    private static boolean matchesPattern(ShapedRecipe recipe, ItemStack[][] grid, int startRow, int startCol) {
        List<String> pattern = recipe.getPattern();

        for (int row = 0; row < pattern.size(); row++) {
            for (int col = 0; col < pattern.get(row).length(); col++) {
                char patternChar = pattern.get(row).charAt(col);
                MaterialQuantifiable patternMaterial = recipe.getIngredientMap().get(patternChar);
                MaterialQuantifiable gridMaterial = MaterialQuantifiable.of(grid[startRow + row][startCol + col]);

                if (!gridMaterial.matches(patternMaterial.getMaterial()) ||
                        gridMaterial.getAmount() < patternMaterial.getAmount()) {
                    if (!ExchangeableType.isExchangeable(gridMaterial.getMaterial(), patternMaterial.getMaterial())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
