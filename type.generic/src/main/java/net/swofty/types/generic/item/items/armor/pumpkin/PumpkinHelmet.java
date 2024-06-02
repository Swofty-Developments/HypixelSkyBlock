package net.swofty.types.generic.item.items.armor.pumpkin;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class PumpkinHelmet implements CustomSkyBlockItem, StandardItem, LeatherColour, Sellable, NotFinishedYet {
    @Override
    public Color getLeatherColour() {
        return new Color(237, 170, 54);
    }

    @Override
    public double getSellValue() {
        return 10;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DEFENSE, 8D)
                .withBase(ItemStatistic.HEALTH, 8D)
                .build();
    }
}
