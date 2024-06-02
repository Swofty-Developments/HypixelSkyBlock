package net.swofty.types.generic.item.impl.recipes;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class ShapelessRecipe extends SkyBlockRecipe<ShapelessRecipe> {
    public static final List<ShapelessRecipe> CACHED_RECIPES = new ArrayList<>();

    private final List<MaterialQuantifiable> ingredientList;
    @Setter
    private SkyBlockItem[] customRecipeDisplay = null;

    public ShapelessRecipe(RecipeType type, SkyBlockItem result,
                           int amount, Function<SkyBlockPlayer, CraftingResult> canCraft) {
        super(result, type, canCraft);

        setAmount(amount);
        this.ingredientList = new ArrayList<>();
    }

    public ShapelessRecipe(RecipeType type, SkyBlockItem result, Function<SkyBlockPlayer, CraftingResult> canCraft) {
        super(result, type, canCraft);

        this.ingredientList = new ArrayList<>();
    }

    public ShapelessRecipe(RecipeType type, SkyBlockItem result, int amount) {
        super(result, type, (player) -> new CraftingResult(true, null));

        setAmount(amount);
        this.ingredientList = new ArrayList<>();
    }

    public ShapelessRecipe(RecipeType type, SkyBlockItem result) {
        super(result, type, (player) -> new CraftingResult(true, null));

        this.ingredientList = new ArrayList<>();
    }

    @Override
    public ShapelessRecipe setResult(SkyBlockItem result) {
        this.result = result;
        return this;
    }

    @Override
    public void init() {
        CACHED_RECIPES.add(this);
    }

    @Override
    public SkyBlockItem[] consume(SkyBlockItem[] stacks) {
        // Start with a copy of the ingredients needed for the recipe
        List<MaterialQuantifiable> materialsToConsume = new ArrayList<>(ingredientList);
        SkyBlockItem[] modifiedStacks = Arrays.copyOf(stacks, stacks.length); // Copy of the input stacks array

        for (int i = 0; i < modifiedStacks.length && !materialsToConsume.isEmpty(); i++) {
            if (modifiedStacks[i] == null) {
                continue; // Skip null SkyBlockItems
            }

            // Get the MaterialQuantifiable representation of the current stack
            MaterialQuantifiable currentStackMaterial = MaterialQuantifiable.of(modifiedStacks[i].getItemStack());
            MaterialQuantifiable toConsume = materialsToConsume.stream()
                    .filter(material -> material.matches(currentStackMaterial.getMaterial())
                            || ExchangeableType.isExchangeable(material.getMaterial(), currentStackMaterial.getMaterial()))
                    .findFirst()
                    .orElse(null);

            if (toConsume != null) {
                int stackAmount = currentStackMaterial.getAmount();
                int consumeAmount = toConsume.getAmount();

                if (stackAmount >= consumeAmount) {
                    // Enough materials in the current stack, subtract the amount
                    currentStackMaterial.setAmount(stackAmount - consumeAmount);
                    materialsToConsume.remove(toConsume); // Remove the consumed material from the recipe
                    // Update the SkyBlockItem with the new amount or set to null if all consumed
                    modifiedStacks[i] = (currentStackMaterial.getAmount() > 0) ?
                            new SkyBlockItem(currentStackMaterial.getMaterial(), currentStackMaterial.getAmount()) : // Ensure toSkyBlockItem correctly represents the quantity
                            null;
                } else {
                    // Not enough materials in the current stack, consume all and reduce needed amount
                    toConsume.setAmount(consumeAmount - stackAmount);
                    modifiedStacks[i] = null; // This stack has been fully consumed
                }
            }
        }

        if (!materialsToConsume.isEmpty()) {
            throw new IllegalStateException("Not enough materials to consume!");
        }

        return modifiedStacks;
    }

    @Override
    public SkyBlockItem[] getRecipeDisplay() {
        if (customRecipeDisplay != null)
            return customRecipeDisplay;
        SkyBlockItem[] display = new SkyBlockItem[9];
        int i = 0;

        for (MaterialQuantifiable material : ingredientList) {
            display[i] = new SkyBlockItem(material.getMaterial());
            display[i].setAmount(material.getAmount());
            i++;
        }

        return display;
    }

    @Override
    public SkyBlockRecipe clone() {
        ShapelessRecipe recipe = new ShapelessRecipe(recipeType, result, amount, canCraft);
        recipe.ingredientList.addAll(ingredientList.stream().map(MaterialQuantifiable::clone).toList());
        return recipe;
    }

    public ShapelessRecipe add(ItemType material, int amount) {
        return add(new MaterialQuantifiable(material, amount));
    }

    public ShapelessRecipe add(MaterialQuantifiable material) {
        ingredientList.add(material.clone());
        return this;
    }

    public static ShapelessRecipe parseShapelessRecipe(ItemStack[] stacks) {
        List<MaterialQuantifiable> materialsPassedThrough = Arrays.stream(stacks)
                .map(MaterialQuantifiable::of)
                .map(MaterialQuantifiable::clone)
                .toList();

        List<ItemType> uniqueMaterials = new ArrayList<>(materialsPassedThrough.stream()
                .map(MaterialQuantifiable::getMaterial)
                .distinct()
                .toList());

        uniqueMaterials.removeIf(material -> {
            try {
                return material.material == null || material.material == Material.AIR;
            } catch (NullPointerException ignored) {
                return true;
            }
        });

        return CACHED_RECIPES.stream()
                .filter(recipe -> {
                    // Check if the recipe has the same amount of materials as the passed through materials
                    // Updated to consider exchangeable materials.
                    List<ItemType> recipeMaterials = recipe.getIngredientList().stream()
                            .map(MaterialQuantifiable::getMaterial)
                            .distinct()
                            .toList();

                    boolean materialsMatch = recipeMaterials.size() == uniqueMaterials.size();

                    for (ItemType recipeMaterial : recipeMaterials) {
                        materialsMatch &= uniqueMaterials.stream()
                                .anyMatch(material -> material == recipeMaterial
                                        || ExchangeableType.isExchangeable(material, recipeMaterial));
                    }

                    return materialsMatch;
                })
                .filter(recipe -> {
                    // Checks if the recipe soft matches, meaning that materialsPassedThrough has at least the same materials as materialsNeeded
                    // and atleast the amount of materialsNeeded, but not necessarily the same amount
                    List<MaterialQuantifiable> materialsNeeded = recipe.getIngredientList().stream()
                            .map(MaterialQuantifiable::new)
                            .map(MaterialQuantifiable::clone)
                            .collect(Collectors.toList());

                    materialsPassedThrough.forEach(material -> {
                        MaterialQuantifiable found = materialsNeeded.stream()
                                .filter(needed -> needed.matches(material.getMaterial())
                                        || ExchangeableType.isExchangeable(needed.getMaterial(), material.getMaterial()))
                                .findFirst()
                                .orElse(null);

                        if (found != null) {
                            int difference = found.getAmount() - material.getAmount();
                            if (difference > 0) {
                                found.setAmount(difference);
                            } else {
                                materialsNeeded.remove(found);
                            }
                        }
                    });
                    return materialsNeeded.isEmpty();
                })
                .findFirst()
                .orElse(null);
    }
}
