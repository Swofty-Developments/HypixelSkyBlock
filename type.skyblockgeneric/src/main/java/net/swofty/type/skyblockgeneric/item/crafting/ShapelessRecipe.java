package net.swofty.type.skyblockgeneric.item.crafting;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.item.ItemType;
import net.swofty.type.skyblockgeneric.item.ItemQuantifiable;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class ShapelessRecipe extends SkyBlockRecipe<ShapelessRecipe> {
    public static final List<ShapelessRecipe> CACHED_RECIPES = new ArrayList<>();

    private final List<ItemQuantifiable> ingredientList;
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
        List<ItemQuantifiable> materialsToConsume = new ArrayList<>(ingredientList);
        SkyBlockItem[] modifiedStacks = Arrays.copyOf(stacks, stacks.length);

        for (int i = 0; i < modifiedStacks.length && !materialsToConsume.isEmpty(); i++) {
            if (modifiedStacks[i] == null) {
                continue;
            }

            ItemQuantifiable currentStackMaterial = ItemQuantifiable.of(modifiedStacks[i].getItemStack());
            ItemQuantifiable toConsume = materialsToConsume.stream()
                    .filter(material -> material.matchesType(currentStackMaterial.getItem())
                            || ExchangeableType.isExchangeable(
                                    material.getItem().getAttributeHandler().getPotentialType(),
                                    currentStackMaterial.getItem().getAttributeHandler().getPotentialType()
                    ))
                    .findFirst()
                    .orElse(null);

            if (toConsume != null) {
                int stackAmount = currentStackMaterial.getAmount();
                int consumeAmount = toConsume.getAmount();

                if (stackAmount >= consumeAmount) {
                    currentStackMaterial.setAmount(stackAmount - consumeAmount);
                    materialsToConsume.remove(toConsume);
                    modifiedStacks[i] = (currentStackMaterial.getAmount() > 0) ?
                            currentStackMaterial.toSkyBlockItem() :
                            null;
                } else {
                    toConsume.setAmount(consumeAmount - stackAmount);
                    modifiedStacks[i] = null;
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

        for (ItemQuantifiable material : ingredientList) {
            display[i] = material.getItem().clone();
            display[i].setAmount(material.getAmount());
            i++;
        }

        return display;
    }

    @Override
    public SkyBlockRecipe<?> clone() {
        ShapelessRecipe recipe = new ShapelessRecipe(recipeType, result, amount, canCraft);
        recipe.ingredientList.addAll(ingredientList.stream().map(ItemQuantifiable::clone).toList());
        return recipe;
    }

    public ShapelessRecipe add(ItemType material, int amount) {
        return add(new ItemQuantifiable(material, amount));
    }

    public ShapelessRecipe add(ItemQuantifiable material) {
        ingredientList.add(material.clone());
        return this;
    }

    public static ShapelessRecipe parseShapelessRecipe(ItemStack[] stacks) {
        List<ItemQuantifiable> materialsPassedThrough = Arrays.stream(stacks)
                .map(ItemQuantifiable::of)
                .map(ItemQuantifiable::clone)
                .toList();

        List<ItemType> uniqueMaterials = new ArrayList<>(materialsPassedThrough.stream()
                .map(iq -> iq.getItem().getAttributeHandler().getPotentialType())
                .distinct()
                .toList());

        uniqueMaterials.removeIf(material -> material == null || material == ItemType.AIR);

        return CACHED_RECIPES.stream()
                .filter(recipe -> {
                    List<ItemType> recipeMaterials = recipe.getIngredientList().stream()
                            .map(iq -> iq.getItem().getAttributeHandler().getPotentialType())
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
                    List<ItemQuantifiable> materialsNeeded = recipe.getIngredientList().stream()
                            .map(ItemQuantifiable::new)
                            .map(ItemQuantifiable::clone)
                            .collect(Collectors.toList());

                    materialsPassedThrough.forEach(material -> {
                        ItemQuantifiable found = materialsNeeded.stream()
                                .filter(needed -> needed.matchesType(material.getItem())
                                        || ExchangeableType.isExchangeable(
                                                needed.getItem().getAttributeHandler().getPotentialType(),
                                                material.getItem().getAttributeHandler().getPotentialType()
                                        ))
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

    @Override
    public String toString() {
        return "ShapelessRecipe{" +
                "recipeType=" + recipeType +
                ", result=" + result +
                ", ingredientList=" + ingredientList +
                ", canCraft=" + canCraft +
                ", amount=" + amount;
    }
}