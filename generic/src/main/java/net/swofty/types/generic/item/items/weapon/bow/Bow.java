package net.swofty.types.generic.item.items.weapon.bow;

import net.swofty.types.generic.item.impl.BowImpl;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.QuiverDisplayOnHold;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class Bow implements CustomSkyBlockItem, BowImpl, QuiverDisplayOnHold {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 30D)
                .build();
    }

    @Override
    public boolean shouldBeArrow() {
        return true;
    }
}
