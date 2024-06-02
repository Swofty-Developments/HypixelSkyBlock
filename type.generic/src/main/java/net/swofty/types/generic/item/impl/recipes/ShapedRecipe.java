package net.swofty.types.generic.item.impl.recipes;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.*;
import java.util.function.Function;

@Getter
public class ShapedRecipe extends SkyBlockRecipe<ShapedRecipe> {
    public static final List<ShapedRecipe> CACHED_RECIPES = new ArrayList<>();

    private final Map<Character, MaterialQuantifiable> ingredientMap;
    private final Map<Character, Function<SkyBlockItem, Boolean>> extraRequirements = new HashMap<>();
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

    public void addExtraRequirement(char patternChar, Function<SkyBlockItem, Boolean> requirement) {
        extraRequirements.put(patternChar, requirement);
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
                Map<Character, MaterialQuantifiable> tempMaterialsToConsume = new HashMap<>(materialsToConsume);
                SkyBlockItem[] tempModifiedStacks = Arrays.copyOf(modifiedStacks, modifiedStacks.length);
                boolean patternMatched = true;

                // Iterate through stacks within the potentially shifted pattern
                for (int row = 0; row < patternRows; row++) {
                    for (int col = 0; col < patternCols; col++) {
                        int gridRow = startRow + row;
                        int gridCol = startCol + col;
                        int index = gridRow * 3 + gridCol;

                        char patternChar = pattern.get(row).charAt(col);
                        MaterialQuantifiable patternMaterial = ingredientMap.get(patternChar);

                        if (patternMaterial != null && !patternMaterial.getMaterial().equals(ItemType.AIR)) {
                            MaterialQuantifiable stackMaterial = MaterialQuantifiable.of(tempModifiedStacks[index].getItemStack());

                            // skip the iteration if stackMaterial is AIR
                            if (stackMaterial.getMaterial() == null || stackMaterial.getMaterial().equals(ItemType.AIR)) {
                                patternMatched = false;
                                break;
                            }

                            if (stackMaterial.matches(patternMaterial.getMaterial())
                                    || ExchangeableType.isExchangeable(stackMaterial.getMaterial(), patternMaterial.getMaterial())) {
                                int stackAmount = stackMaterial.getAmount();
                                int consumeAmount = patternMaterial.getAmount();

                                if (stackAmount >= consumeAmount) {
                                    stackMaterial.setAmount(stackAmount - consumeAmount);
                                    tempMaterialsToConsume.remove(patternChar);

                                    tempModifiedStacks[index] = stackMaterial.getAmount() > 0 ? stackMaterial.toSkyBlockItem() : null;
                                } else {
                                    patternMatched = false;
                                    break;
                                }
                            } else {
                                patternMatched = false;
                                break;
                            }
                        }
                    }

                    if (!patternMatched) {
                        break;
                    }
                }

                // If all of the materials were consumed and the pattern matched, update the original stacks and return
                if (tempMaterialsToConsume.isEmpty() && patternMatched) {
                    modifiedStacks = tempModifiedStacks;
                    return modifiedStacks;
                }
            }
        }

        // If there are still materials left to consume or the pattern didn't match, there were not enough materials in the stacks
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

    public Map<Character, List<Integer>> getPositionsOfItems(ItemStack[] stacks) {
        Map<Character, List<Integer>> positions = new HashMap<>();

        int patternRows = pattern.size();
        int patternCols = pattern.get(0).length();

        // Try all possible starting positions (top-left corners) for the pattern in the grid
        for (int startRow = 0; startRow <= 3 - patternRows; startRow++) {
            for (int startCol = 0; startCol <= 3 - patternCols; startCol++) {

                // Iterate through stacks within the potentially shifted pattern
                for (int i = 0; i < stacks.length; i++) {
                    int gridRow = i / 3;
                    int gridCol = i % 3;

                    // If this stack is within our shifted pattern on the grid
                    if (gridRow >= startRow && gridRow < startRow + patternRows &&
                            gridCol >= startCol && gridCol < startCol + patternCols) {

                        char patternChar = pattern.get(gridRow - startRow).charAt(gridCol - startCol);
                        MaterialQuantifiable patternMaterial = ingredientMap.get(patternChar);

                        if (patternMaterial != null && !patternMaterial.getMaterial().equals(ItemType.AIR)) {
                            MaterialQuantifiable stackMaterial = MaterialQuantifiable.of(stacks[i]);

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
                                    positions.computeIfAbsent(patternChar, k -> new ArrayList<>()).add(i);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Remove duplicate positions from the same character
        positions.forEach((character, positionsList) -> {
            Set<Integer> positionsSet = new HashSet<>(positionsList);
            positionsList.clear();
            positionsList.addAll(positionsSet);
        });

        return positions;
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
                    int patternCols = recipePattern.getFirst().length();

                    for (int row = 0; row <= 3 - patternRows; row++) {
                        for (int col = 0; col <= 3 - patternCols; col++) {
                            try {
                                if (matchesPattern(recipe, grid, row, col)) {
                                    return true;
                                }
                            } catch (Exception e) {
                                Logger.error("Error in recipe " + recipe.getResult().toString() + " at row " + row + " col " + col);
                            }
                        }
                    }

                    return false;
                })
                .filter(recipe -> {
                    for (Map.Entry<Character, List<Integer>> entry : recipe.getPositionsOfItems(stacks).entrySet()) {
                        Character character = entry.getKey();

                        Function<SkyBlockItem, Boolean> extraRequirements = recipe.getExtraRequirements().get(character);
                        if (extraRequirements == null) {
                            continue;
                        }

                        for (int position : entry.getValue()) {
                            SkyBlockItem item = new SkyBlockItem(stacks[position]);
                            if (!extraRequirements.apply(item)) {
                                return false;
                            }
                        }
                    }

                    return true;
                })
                .max(Comparator.comparing(recipe -> {
                    int patternRows = recipe.getPattern().size();
                    int patternCols = recipe.getPattern().getFirst().length();

                    return patternRows * patternCols;
                }))
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
