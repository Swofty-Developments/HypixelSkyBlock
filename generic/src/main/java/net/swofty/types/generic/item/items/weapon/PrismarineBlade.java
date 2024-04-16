package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class PrismarineBlade implements CustomSkyBlockItem, StandardItem, Sellable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.DAMAGE, 50D)
                .withAdditive(ItemStatistic.STRENGTH, 25D)
                .build();
    }

    @Override
    public double getSellValue() {
        return 160;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
