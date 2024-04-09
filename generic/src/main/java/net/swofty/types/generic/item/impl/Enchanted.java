package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public interface Enchanted extends CustomSkyBlockItem {
    @Override
    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.EMPTY;
    }

    default SkyBlockRecipe<?> getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType type, ItemType craftingMaterial) {
        return SkyBlockRecipe.getStandardEnchantedRecipe(this.getClass(), type, craftingMaterial);
    }
}
