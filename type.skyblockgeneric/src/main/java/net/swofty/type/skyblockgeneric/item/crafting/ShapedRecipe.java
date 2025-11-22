package net.swofty.type.skyblockgeneric.item.crafting;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.type.skyblockgeneric.item.ItemQuantifiable;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.function.Function;

@Getter
public class ShapedRecipe extends SkyBlockRecipe<ShapedRecipe> {
    public static final List<ShapedRecipe> CACHED_RECIPES = new ArrayList<>();

    private final Map<Character, ItemQuantifiable> ingredientMap;
    private final Map<Character, Function<SkyBlockItem, Boolean>> extraRequirements = new HashMap<>();

    private final List<String> pattern;
    private final char[][] patternArray;
    private final int height;
    private final int width;

    public ShapedRecipe(RecipeType type,
                        SkyBlockItem result,
                        Map<Character, ItemQuantifiable> ingredientMap,
                        List<String> pattern,
                        Function<SkyBlockPlayer, CraftingResult> canCraft) {
        super(result, type, canCraft);
        this.ingredientMap = new HashMap<>(ingredientMap);

        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must have at least one element");
        }

        this.pattern = normalizedPatternList(pattern);
        this.height = this.pattern.size();
        this.width = this.pattern.stream().mapToInt(String::length).max().orElse(0);
        this.patternArray = toCharArray(this.pattern, height, width);

        this.ingredientMap.putIfAbsent(' ', new ItemQuantifiable(ItemType.AIR, 0));
        this.ingredientMap.putIfAbsent('O', new ItemQuantifiable(ItemType.AIR, 0));
    }

    public ShapedRecipe(RecipeType type,
                        SkyBlockItem result,
                        Map<Character, ItemQuantifiable> ingredientMap,
                        List<String> pattern) {
        this(type, result, ingredientMap, pattern, (_) -> new CraftingResult(true, new String[]{}));
    }

    private List<String> normalizedPatternList(List<String> pattern) {
        int maxLength = pattern.stream().mapToInt(String::length).max().orElse(0);
        List<String> out = new ArrayList<>(pattern.size());
        for (String row : pattern) {
            if (row.length() < maxLength) {
                out.add(String.format("%-" + maxLength + "s", row));
            } else {
                out.add(row);
            }
        }
        return out;
    }

    private char[][] toCharArray(List<String> paddedPattern, int h, int w) {
        char[][] arr = new char[h][w];
        for (int i = 0; i < h; i++) {
            String row = paddedPattern.get(i);
            for (int j = 0; j < w; j++) {
                arr[i][j] = row.charAt(j);
            }
        }
        return arr;
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

    private boolean matchesAtPosition(ItemStack[] stacks, int startRow, int startCol) {
        for (int row = 0; row < 3; row++) {
            int patternRow = row - startRow;
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                char symbol = 'O';

                int patternCol = col - startCol;
                if (patternRow >= 0 && patternRow < height && patternCol >= 0 && patternCol < width) {
                    symbol = patternArray[patternRow][patternCol];
                }

                ItemQuantifiable expected = ingredientMap.get(symbol);
                ItemQuantifiable actual = ItemQuantifiable.of(stacks[index]);

                if (symbol == 'O' || expected == null || expected.getItem().getMaterial() == Material.AIR) {
                    if (actual.getItem().getMaterial() != Material.AIR) return false;
                    continue;
                }

                if (actual.getAmount() < expected.getAmount()) return false;

                if (actual.getItem().getMaterial() == Material.AIR) return false;
                if (!actual.matchesType(expected.getItem())) {
                    if (!ExchangeableType.isExchangeable(
                            actual.getItem().getAttributeHandler().getPotentialType(),
                            expected.getItem().getAttributeHandler().getPotentialType())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public SkyBlockItem[] consume(SkyBlockItem[] stacks) {
        ItemStack[] rawStacks = new ItemStack[stacks.length];
        for (int i = 0; i < stacks.length; i++) rawStacks[i] = (stacks[i] != null) ? stacks[i].getItemStack() : null;

        for (int startRow = 0; startRow <= 3 - height; startRow++) {
            for (int startCol = 0; startCol <= 3 - width; startCol++) {
                if (matchesAtPosition(rawStacks, startRow, startCol)) {
                    SkyBlockItem[] resultStacks = Arrays.copyOf(stacks, stacks.length);

                    for (int r = 0; r < height; r++) {
                        for (int c = 0; c < width; c++) {
                            char symbol = patternArray[r][c];
                            ItemQuantifiable required = ingredientMap.get(symbol);
                            if (required == null || required.getItem().getMaterial() == Material.AIR) continue;

                            int index = (startRow + r) * 3 + (startCol + c);
                            SkyBlockItem slot = resultStacks[index];

                            if (slot != null && slot.getMaterial() != Material.AIR) {
                                int newAmount = slot.getAmount() - required.getAmount();
                                resultStacks[index] = (newAmount > 0) ?
                                        new SkyBlockItem(slot.getItemStack().withAmount(newAmount))
                                        : null;
                            }
                        }
                    }
                    return resultStacks;
                }
            }
        }

        throw new IllegalStateException("Not enough materials to consume!");
    }

    @Override
    public SkyBlockItem[] getRecipeDisplay() {
        SkyBlockItem[] display = new SkyBlockItem[9];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char symbol = patternArray[r][c];
                ItemQuantifiable itemQ = ingredientMap.get(symbol);

                if (itemQ != null && itemQ.getItem() != null && itemQ.getItem().getMaterial() != Material.AIR) {
                    display[r * 3 + c] = itemQ.getItem().clone();
                }
            }
        }

        return display;
    }

    @Override
    public SkyBlockRecipe<?> clone() {
        List<String> patternCopy = new ArrayList<>(this.pattern);
        return new ShapedRecipe(recipeType, result.clone(), new HashMap<>(ingredientMap), patternCopy, canCraft);
    }

    public static ShapedRecipe parseShapedRecipe(ItemStack[] stacks) {
        ShapedRecipe best = null;
        int bestSize = -1;

        for (ShapedRecipe recipe : CACHED_RECIPES) {
            int size = recipe.height * recipe.width;

            outerRow:
            for (int row = 0; row <= 3 - recipe.height; row++) {
                for (int col = 0; col <= 3 - recipe.width; col++) {
                    if (!recipe.matchesAtPosition(stacks, row, col)) continue;

                    Map<Character, List<Integer>> positions = new HashMap<>();
                    for (int r = 0; r < recipe.height; r++) {
                        for (int c = 0; c < recipe.width; c++) {
                            char symbol = recipe.patternArray[r][c];
                            ItemQuantifiable expected = recipe.ingredientMap.get(symbol);
                            if (expected != null && expected.getItem().getMaterial() != Material.AIR) {
                                int index = (row + r) * 3 + (col + c);
                                positions.computeIfAbsent(symbol, k -> new ArrayList<>()).add(index);
                            }
                        }
                    }

                    for (Map.Entry<Character, List<Integer>> entry : positions.entrySet()) {
                        Function<SkyBlockItem, Boolean> req = recipe.extraRequirements.get(entry.getKey());
                        if (req == null) continue;

                        for (int idx : entry.getValue()) {
                            SkyBlockItem sbi = (stacks[idx] != null) ? new SkyBlockItem(stacks[idx]) : null;
                            if (sbi == null || !req.apply(sbi)) {
                                continue outerRow; // this offset fails -> try next offset
                            }
                        }
                    }

                    // this recipe+offset passed all checks -> consider as candidate
                    if (size > bestSize) {
                        best = recipe;
                        bestSize = size;
                    }
                }
            }
        }

        return best;
    }

    public Map<Character, List<Integer>> getPositionsOfItems(ItemStack[] stacks) {
        Map<Character, List<Integer>> positions = new HashMap<>();

        for (int startRow = 0; startRow <= 3 - height; startRow++) {
            for (int startCol = 0; startCol <= 3 - width; startCol++) {
                if (matchesAtPosition(stacks, startRow, startCol)) {
                    for (int r = 0; r < height; r++) {
                        for (int c = 0; c < width; c++) {
                            int index = (startRow + r) * 3 + (startCol + c);
                            char symbol = patternArray[r][c];
                            ItemQuantifiable expected = ingredientMap.get(symbol);

                            if (expected != null && expected.getItem().getMaterial() != Material.AIR) {
                                positions.computeIfAbsent(symbol, k -> new ArrayList<>()).add(index);
                            }
                        }
                    }
                }
            }
        }

        positions.replaceAll((k, v) -> new ArrayList<>(new LinkedHashSet<>(v)));
        return positions;
    }

    @Override
    public String toString() {
        return "ShapedRecipe{" +
                "recipeType=" + recipeType +
                ", result=" + result +
                ", ingredientMap=" + ingredientMap +
                ", pattern=" + pattern +
                ", canCraft=" + canCraft +
                ", amount=" + amount +
                '}';
    }
}