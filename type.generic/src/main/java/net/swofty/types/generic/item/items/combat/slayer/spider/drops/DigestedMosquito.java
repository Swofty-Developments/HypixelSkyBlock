package net.swofty.types.generic.item.items.combat.slayer.spider.drops;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.commons.statistics.ItemStatistics;

public class DigestedMosquito implements CustomSkyBlockItem, RightClickRecipe {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ItemTypeLinker getRecipeItem() {
        return ItemTypeLinker.MOSQUITO_BOW;
    }
}
