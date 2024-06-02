package net.swofty.types.generic.item.items.armor.farmsuit;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;


public class FarmSuitHelmet implements CustomSkyBlockItem, StandardItem, LeatherColour, Sellable, NotFinishedYet {

    @Override
    public Color getLeatherColour() {
        return new Color(255, 255, 0);
    }

    @Override
    public double getSellValue() {
        return 25;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DEFENSE, 15D)
                .withBase(ItemStatistic.FARMING_FORTUNE, 5D)
                .build();
    }
}
