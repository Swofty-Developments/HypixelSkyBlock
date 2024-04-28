package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ArrowImpl;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class FlintArrow implements CustomSkyBlockItem, ArrowImpl {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 1D)
                .build();
    }
}
