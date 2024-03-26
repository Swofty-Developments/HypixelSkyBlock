package net.swofty.types.generic.item.items.weapon.bow;

import net.swofty.types.generic.item.impl.ArrowImpl;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class Arrow implements CustomSkyBlockItem, ArrowImpl {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 1D)
                .build();
    }
}
