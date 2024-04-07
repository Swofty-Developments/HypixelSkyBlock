package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Soulbound;
import net.swofty.types.generic.item.impl.SwordImpl;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class HunterKnife implements CustomSkyBlockItem, SwordImpl, Soulbound {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 50D)
                .build();
    }

    @Override
    public boolean isSoulbound() {
        return true;
    }
}
