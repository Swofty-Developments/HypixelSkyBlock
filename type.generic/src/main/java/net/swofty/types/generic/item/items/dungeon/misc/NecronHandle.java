package net.swofty.types.generic.item.items.dungeon.misc;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.commons.statistics.ItemStatistics;

public class NecronHandle implements CustomSkyBlockItem, Enchanted, RightClickRecipe {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ItemTypeLinker getRecipeItem() {
        return ItemTypeLinker.NECRONS_HANDLE;
    }
}
