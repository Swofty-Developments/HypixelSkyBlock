package net.swofty.types.generic.item.impl;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.SkyBlockItem;

public interface PowerStone extends ExtraRarityDisplay, CustomSkyBlockItem {

    @Override
    default String getExtraRarityDisplay() {
        return " POWER STONE";
    }

    @Override
    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
}
