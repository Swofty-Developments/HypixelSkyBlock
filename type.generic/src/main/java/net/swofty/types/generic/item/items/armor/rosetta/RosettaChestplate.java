package net.swofty.types.generic.item.items.armor.rosetta;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class RosettaChestplate implements CustomSkyBlockItem, CustomDisplayName, StandardItem, Sellable {
    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "Rosetta's Chestplate";
    }

    @Override
    public double getSellValue() {
        return 660;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.CHESTPLATE;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 15D)
                .withBase(ItemStatistic.DEFENSE, 40D)
                .build();
    }
}
