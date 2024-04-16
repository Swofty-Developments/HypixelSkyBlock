package net.swofty.types.generic.item.items.armor.starlight;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class StarlightHelmet implements CustomSkyBlockItem, StandardItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 30D)
                .withAdditive(ItemStatistic.DEFENSE, 30D)
                .withAdditive(ItemStatistic.INTELLIGENCE, 50D)
                .build();
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}
