package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SwordImpl;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class FancySword implements CustomSkyBlockItem, SwordImpl, Sellable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 50D)
                .with(ItemStatistic.SPEED, 40D)
                .build();
    }

    @Override
    public double getSellValue() {
        return 50;
    }
}
