package net.swofty.types.generic.item.items.foraging;

import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Reforgable;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class RookieAxe implements CustomSkyBlockItem, Reforgable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 20D)
                .build();
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.AXES;
    }
}
