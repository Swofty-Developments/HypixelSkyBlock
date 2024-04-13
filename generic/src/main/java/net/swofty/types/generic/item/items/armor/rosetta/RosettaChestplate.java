package net.swofty.types.generic.item.items.armor.rosetta;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class RosettaChestplate implements CustomSkyBlockItem, CustomDisplayName, StandardItem {
    @Override
    public String getDisplayName() {
        return "Rosetta's Chestplate";
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .with(ItemStatistic.HEALTH, 15D)
                .with(ItemStatistic.DEFENSE, 40D)
                .build();
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.CHESTPLATE;
    }
}
