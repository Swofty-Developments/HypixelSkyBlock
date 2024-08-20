package net.swofty.types.generic.item.impl;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;

import java.util.List;

public interface Sack extends TrackedUniqueItem, Interactable, CustomSkyBlockItem {
    List<ItemTypeLinker> getSackItems();
    int getMaximumCapacity();
    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
}
