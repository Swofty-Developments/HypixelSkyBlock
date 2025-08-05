package net.swofty.types.generic.item.crafting;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

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
        this.ingredientMap = new HashMap<>(ingredientMap);
        this.pattern = normalizedPattern(pattern);
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must have at least one element");
        }
        this.ingredientMap.putIfAbsent(' ', new ItemQuantifiable(ItemType.AIR, 0));
        this.ingredientMap.putIfAbsent('O', new ItemQuantifiable(ItemType.AIR, 0));
    }

    public ShapedRecipe(RecipeType type,
                        SkyBlockItem result, Map<Character, ItemQuantifiable> ingredientMap,
                        List<String> pattern) {
        this(type, result, ingredientMap, pattern, (_) -> new CraftingResult(true, new String[]{}));
    }

    private List<String> normalizedPattern(List<String> pattern) {
        int maxLength = pattern.stream().mapToInt(String::length).max().orElse(0);
        return pattern.stream()
                .map(row -> String.format("%-" + maxLength + "s", row))
                .toList();
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
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                char symbol = 'O';

                if (row >= startRow && row < startRow + pattern.size()
                        && col >= startCol && col < startCol + pattern.get(0).length()) {
                    symbol = pattern.get(row - startRow).charAt(col - startCol);
                }

                ItemQuantifiable expected = ingredientMap.get(symbol);
                ItemQuantifiable actual = ItemQuantifiable.of(stacks[index]);

                if (symbol == 'O' || expected == null || expected.getItem().getMaterial() == Material.AIR) {
                    if (actual.getItem().getMaterial() != Material.AIR) return false;
                    continue;
                }

                boolean typeMatch = actual.matchesType(expected.getItem()) ||
                        ExchangeableType.isExchangeable(
                                actual.getItem().getAttributeHandler().getPotentialType(),
                                expected.getItem().getAttributeHandler().getPotentialType());
                if (!typeMatch || actual.getAmount() < expected.getAmount()) return false;
            }
        }
        return true;
    }

    @Override
    public SkyBlockItem[] consume(SkyBlockItem[] stacks) {
        for (int startRow = 0; startRow <= 3 - pattern.size(); startRow++) {
            for (int startCol = 0; startCol <= 3 - pattern.get(0).length(); startCol++) {

                if (matchesAtPosition(Arrays.stream(stacks).map(s -> {
                    return s != null ? s.getItemStack() : null;
                }).toArray(ItemStack[]::new), startRow, startCol)) {
                    SkyBlockItem[] resultStacks = Arrays.copyOf(stacks, stacks.length);
                    Map<Character, Integer> remaining = new HashMap<>();
                    ingredientMap.forEach((k, v) -> remaining.put(k, v.getAmount()));

                    for (int row = 0; row < pattern.size(); row++) {
                        for (int col = 0; col < pattern.get(row).length(); col++) {
                            int index = (startRow + row) * 3 + (startCol + col);
                            char symbol = pattern.get(row).charAt(col);
                            ItemQuantifiable required = ingredientMap.get(symbol);

                            if (required == null || required.getItem().getMaterial() == Material.AIR) continue;
                            SkyBlockItem slot = resultStacks[index];

                            if (slot != null && slot.getMaterial() != Material.AIR) {
                                int newAmount = slot.getAmount() - required.getAmount();
                                resultStacks[index] = (newAmount > 0) ?
                                        new SkyBlockItem(slot.getItemStack().withAmount(newAmount))
                                        : null;
                                remaining.put(symbol, remaining.get(symbol) - required.getAmount());
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

        for (int row = 0; row < pattern.size(); row++) {
            for (int col = 0; col < pattern.get(row).length(); col++) {
                char symbol = pattern.get(row).charAt(col);
                ItemQuantifiable item = ingredientMap.get(symbol);

                if (item != null && item.getItem() != null
                        && item.getItem().getMaterial() != Material.AIR) {
                    display[row * 3 + col] = item.getItem().clone();
                }
            }
        }

        return display;
    }

    @Override
    public SkyBlockRecipe<?> clone() {
        return new ShapedRecipe(recipeType, result.clone(), new HashMap<>(ingredientMap), new ArrayList<>(pattern), canCraft);
    }

    public static ShapedRecipe parseShapedRecipe(ItemStack[] stacks) {
        return CACHED_RECIPES.stream()
                .filter(recipe -> {
                    for (int row = 0; row <= 3 - recipe.getPattern().size(); row++) {
                        for (int col = 0; col <= 3 - recipe.getPattern().get(0).length(); col++) {
                            if (recipe.matchesAtPosition(stacks, row, col)) {
                                for (var entry : recipe.getPositionsOfItems(stacks).entrySet()) {
                                    var req = recipe.getExtraRequirements().get(entry.getKey());

                                    if (req != null) {
                                        for (int i : entry.getValue()) {
                                            if (!req.apply(new SkyBlockItem(stacks[i]))) return false;
                                        }
                                    }
                                }
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .max(Comparator.comparing(r -> r.getPattern().size() * r.getPattern().get(0).length()))
                .orElse(null);
    }

    public Map<Character, List<Integer>> getPositionsOfItems(ItemStack[] stacks) {
        Map<Character, List<Integer>> positions = new HashMap<>();

        for (int startRow = 0; startRow <= 3 - pattern.size(); startRow++) {
            for (int startCol = 0; startCol <= 3 - pattern.get(0).length(); startCol++) {

                if (matchesAtPosition(stacks, startRow, startCol)) {
                    for (int row = 0; row < pattern.size(); row++) {
                        for (int col = 0; col < pattern.get(row).length(); col++) {
                            int index = (startRow + row) * 3 + (startCol + col);
                            char symbol = pattern.get(row).charAt(col);
                            ItemQuantifiable expected = ingredientMap.get(symbol);

                            if (expected != null && expected.getItem().getMaterial() != Material.AIR) {
                                positions.computeIfAbsent(symbol, __ -> new ArrayList<>()).add(index);
                            }
                        }
                    }
                }
            }
        }

        positions.replaceAll((k, v) -> new ArrayList<>(new HashSet<>(v)));
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