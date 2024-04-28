package net.swofty.types.generic.item.items.armor.leaflet;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class LeafletBoots implements CustomSkyBlockItem, StandardItem, LeatherColour, Sellable, CustomDisplayName {
    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "Leaflet Sandals";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(77, 204, 77);
    }

    @Override
    public double getSellValue() {
        return 2;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.BOOTS;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder().withBase(ItemStatistic.HEALTH, 15D).build();
    }
}
