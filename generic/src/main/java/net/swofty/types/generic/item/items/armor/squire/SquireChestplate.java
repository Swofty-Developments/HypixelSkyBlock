package net.swofty.types.generic.item.items.armor.squire;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class SquireChestplate implements CustomSkyBlockItem, StandardItem, Sellable {
    @Override
    public double getSellValue() {
        return 4000;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.CHESTPLATE;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 40D)
                .withBase(ItemStatistic.DEFENSE, 40D)
                .build();
    }
}
