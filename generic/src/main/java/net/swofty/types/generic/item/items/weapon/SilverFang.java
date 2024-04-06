package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.SwordImpl;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class SilverFang implements CustomSkyBlockItem, SwordImpl, Enchanted {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 100D)
                .build();
    }
}
