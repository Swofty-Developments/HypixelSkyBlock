package net.swofty.item.impl.recipes;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.ItemStack;
import net.swofty.item.ItemType;
import net.swofty.item.MaterialQuantifiable;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class ShapelessRecipe extends SkyBlockRecipe<ShapelessRecipe> {
    public static final List<ShapelessRecipe> CACHED_RECIPES = new ArrayList<>();

    private final List<MaterialQuantifiable> ingredientList;

    public ShapelessRecipe(SkyBlockItem result, int amount, Function<SkyBlockPlayer, CraftingResult> canCraft) {
        super(result, canCraft);

        setAmount(amount);
        this.ingredientList = new ArrayList<>();
    }

    public ShapelessRecipe(SkyBlockItem result, Function<SkyBlockPlayer, CraftingResult> canCraft) {
        super(result, canCraft);

        this.ingredientList = new ArrayList<>();
    }

    public ShapelessRecipe(SkyBlockItem result, int amount) {
        super(result, (player) -> new CraftingResult(true, null));

        setAmount(amount);
        this.ingredientList = new ArrayList<>();
    }

    public ShapelessRecipe(SkyBlockItem result) {
        super(result, (player) -> new CraftingResult(true, null));

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
                    SkyBlockItem item = new SkyBlockItem(currentStackMaterial.getMaterial());
                    item.setAmount(currentStackMaterial.getAmount());
                    modifiedStacks[i] = (currentStackMaterial.getAmount() > 0) ?
                            item : // Ensure toItemStack correctly represents the quantity
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

        return CACHED_RECIPES.stream()
                .filter(recipe -> {
                    List<MaterialQuantifiable> materialsNeeded = recipe.getIngredientList().stream()
                            .map(MaterialQuantifiable::new)
                            .map(MaterialQuantifiable::clone)
                            .collect(Collectors.toList());
                    // Checks if the recipe soft matches, meaning that materialsPassedThrough has at least the same materials as materialsNeeded
                    // and atleast the amount of materialsNeeded, but not necessarily the same amount
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
