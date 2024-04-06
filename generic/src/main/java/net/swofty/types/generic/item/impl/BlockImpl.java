package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.user.statistics.ItemStatistics;

public interface BlockImpl extends CustomSkyBlockItem {

    @Override
    default ItemStatistics getStatistics(){
        return ItemStatistics.EMPTY;
    }

    @Override
    default boolean isPlaceable() {
        return true;
    }
}
