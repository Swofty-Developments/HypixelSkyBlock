package net.swofty.commons.skyblock.item.impl;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.recipes.ShapelessRecipe;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;

import java.util.Arrays;
import java.util.List;

public interface Enchanted extends CustomSkyBlockItem, Craftable {
    @Override
    default ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    ItemType getCraftingMaterial();

    SkyBlockRecipe.RecipeType getRecipeType();

    @Override
    default SkyBlockRecipe<?> getRecipe() {
        List<ItemType> matchTypes = Arrays.stream(ItemType.values())
                .filter(itemType -> itemType.clazz != null)
                .filter(itemType -> itemType.clazz.equals(this.getClass()))
                .toList();

        if (matchTypes.isEmpty()) {
            throw new RuntimeException("No matching ItemType found");
        } else {
            return new ShapelessRecipe(getRecipeType(), new SkyBlockItem(matchTypes.get(0)))
                    .add(this.getCraftingMaterial(), 64)
                    .add(this.getCraftingMaterial(), 64)
                    .add(this.getCraftingMaterial(), 64)
                    .add(this.getCraftingMaterial(), 64)
                    .add(this.getCraftingMaterial(), 32);
        }
    }
}
