package net.swofty.types.generic.item.items.armor.leaflet;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class LeafletHelmet implements CustomSkyBlockItem, StandardItem, Sellable, CustomDisplayName {
    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Leaflet Hat";
    }

    @Override
    public double getSellValue() {
        return 2;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder().withBase(ItemStatistic.HEALTH, 20D).build();
    }
}
