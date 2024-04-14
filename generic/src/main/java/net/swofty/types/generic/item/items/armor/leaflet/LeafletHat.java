package net.swofty.types.generic.item.items.armor.leaflet;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class LeafletHat implements CustomSkyBlockItem, StandardItem, Sellable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder().with(ItemStatistic.HEALTH, 20D).build();
    }

    @Override
    public double getSellValue() {
        return 10;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}
