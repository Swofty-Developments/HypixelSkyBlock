package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.Arrays;
import java.util.List;

public interface Enchanted extends CustomSkyBlockItem {
    @Override
    default ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    default SkyBlockRecipe<?> getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType type, ItemType craftingMaterial) {
        return SkyBlockRecipe.getStandardEnchantedRecipe(this.getClass(), type, craftingMaterial);
    }
}
