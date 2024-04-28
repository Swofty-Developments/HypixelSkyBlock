package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class MercenaryAxe implements CustomSkyBlockItem, StandardItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 70D)
                .withBase(ItemStatistic.STRENGTH, 20D)
                .build();
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
