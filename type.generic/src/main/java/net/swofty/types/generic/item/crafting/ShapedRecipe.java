package net.swofty.types.generic.item.crafting;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.*;
import java.util.function.Function;

@Getter
public class ShapedRecipe extends SkyBlockRecipe<ShapedRecipe> {
    public static final List<ShapedRecipe> CACHED_RECIPES = new ArrayList<>();

    private final Map<Character, ItemQuantifiable> ingredientMap;
    private final Map<Character, Function<SkyBlockItem, Boolean>> extraRequirements = new HashMap<>();
    private final List<String> pattern;

    public ShapedRecipe(RecipeType type,
                        SkyBlockItem result, Map<Character, ItemQuantifiable> ingredientMap,
                        List<String> pattern, Function<SkyBlockPlayer, CraftingResult> canCraft) {
        super(result, type, canCraft);
        this.ingredientMap = ingredientMap;
        this.pattern = pattern;
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must have at least one element");
        }
    }

    public ShapedRecipe(RecipeType type,
                        SkyBlockItem result, Map<Character, ItemQuantifiable> ingredientMap,
                        List<String> pattern) {
        this(type, result, ingredientMap, pattern, (_) -> new CraftingResult(true, new String[]{}));
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
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>(this.ingredientMap);
        ingredientMap.remove(' ');

        Map<Character, ItemQuantifiable> materialsToConsume = new HashMap<>(ingredientMap);
        SkyBlockItem[] modifiedStacks = Arrays.copyOf(stacks, stacks.length);

        int patternRows = pattern.size();
        int patternCols = pattern.getFirst().length();

        for (int startRow = 0; startRow <= 3 - patternRows; startRow++) {
            for (int startCol = 0; startCol <= 3 - patternCols; startCol++) {
                Map<Character, ItemQuantifiable> tempMaterialsToConsume = new HashMap<>(materialsToConsume);
                SkyBlockItem[] tempModifiedStacks = Arrays.copyOf(modifiedStacks, modifiedStacks.length);
                boolean patternMatched = true;

                for (int row = 0; row < patternRows; row++) {
                    for (int col = 0; col < patternCols; col++) {
                        int gridRow = startRow + row;
                        int gridCol = startCol + col;
                        int index = gridRow * 3 + gridCol;

                        char patternChar = pattern.get(row).charAt(col);
                        ItemQuantifiable patternMaterial = ingredientMap.get(patternChar);

                        if (patternMaterial != null && !patternMaterial.getItem().getMaterial().equals(Material.AIR)) {
                            ItemQuantifiable stackMaterial = ItemQuantifiable.of(tempModifiedStacks[index].getItemStack());

                            if (stackMaterial.getItem().getMaterial() == null || stackMaterial.getItem().getMaterial().equals(Material.AIR)) {
                                patternMatched = false;
                                break;
                            }

                            if (stackMaterial.matchesMaterial(patternMaterial.getItem())
                                    || ExchangeableType.isExchangeable(
                                            stackMaterial.getItem().getAttributeHandler().getPotentialType(),
                                            patternMaterial.getItem().getAttributeHandler().getPotentialType()
                            )) {
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

                if (tempMaterialsToConsume.isEmpty() && patternMatched) {
                    modifiedStacks = tempModifiedStacks;
                    return modifiedStacks;
                }
            }
        }

        throw new IllegalStateException("Not enough materials to consume!");
    }

    @Override
    public SkyBlockItem[] getRecipeDisplay() {
        SkyBlockItem[] recipeDisplay = new SkyBlockItem[9];
        int patternRows = pattern.size();
        int patternCols = pattern.getFirst().length();

        for (int row = 0; row < patternRows; row++) {
            for (int col = 0; col < patternCols; col++) {
                char patternChar = pattern.get(row).charAt(col);
                ItemQuantifiable patternMaterial = ingredientMap.get(patternChar);

                if (patternMaterial != null) {
                    recipeDisplay[row * 3 + col] = patternMaterial.getItem();
                }
            }
        }

        return recipeDisplay;
    }

    @Override
    public SkyBlockRecipe<?> clone() {
        return new ShapedRecipe(recipeType, result, ingredientMap, pattern, canCraft);
    }

    public Map<Character, List<Integer>> getPositionsOfItems(ItemStack[] stacks) {
        Map<Character, List<Integer>> positions = new HashMap<>();

        int patternRows = pattern.size();
        int patternCols = pattern.getFirst().length();

        for (int startRow = 0; startRow <= 3 - patternRows; startRow++) {
            for (int startCol = 0; startCol <= 3 - patternCols; startCol++) {

                for (int i = 0; i < stacks.length; i++) {
                    int gridRow = i / 3;
                    int gridCol = i % 3;

                    if (gridRow >= startRow && gridRow < startRow + patternRows &&
                            gridCol >= startCol && gridCol < startCol + patternCols) {

                        char patternChar = pattern.get(gridRow - startRow).charAt(gridCol - startCol);
                        ItemQuantifiable patternMaterial = ingredientMap.get(patternChar);

                        if (patternMaterial != null && !patternMaterial.getItem().getMaterial().equals(Material.AIR)) {
                            ItemQuantifiable stackMaterial = ItemQuantifiable.of(stacks[i]);

                            if (stackMaterial.getItem().getMaterial().equals(Material.AIR)) {
                                continue;
                            }

                            if (stackMaterial.matchesMaterial(patternMaterial.getItem())
                                    || ExchangeableType.isExchangeable(
                                            stackMaterial.getItem().getAttributeHandler().getPotentialType(),
                                            patternMaterial.getItem().getAttributeHandler().getPotentialType()
                            )) {
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
                                e.printStackTrace();
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
                ItemQuantifiable patternMaterial = recipe.getIngredientMap().get(patternChar);
                ItemQuantifiable gridMaterial = ItemQuantifiable.of(grid[startRow + row][startCol + col]);

                if (!gridMaterial.matchesMaterial(patternMaterial.getItem()) ||
                        gridMaterial.getAmount() < patternMaterial.getAmount()) {
                    if (!ExchangeableType.isExchangeable(gridMaterial.getItem().getAttributeHandler().getPotentialType(),
                            patternMaterial.getItem().getAttributeHandler().getPotentialType())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}