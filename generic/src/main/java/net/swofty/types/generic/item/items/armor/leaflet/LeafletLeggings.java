package net.swofty.types.generic.item.items.armor.leaflet;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class LeafletLeggings implements CustomSkyBlockItem, StandardItem, LeatherColour, Sellable, CustomDisplayName {

    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "Leaflet Pants";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(77, 204, 77);
    }

    @Override
    public double getSellValue() {
        return 3;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.LEGGINGS;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder().withBase(ItemStatistic.HEALTH, 20D).build();
    }
}
