package net.swofty.types.generic.item.items.armor.farmsuit;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;


public class FarmSuitChestplate implements CustomSkyBlockItem, StandardItem, LeatherColour, Sellable, NotFinishedYet {

    @Override
    public Color getLeatherColour() {
        return new Color(255, 255, 0);
    }

    @Override
    public double getSellValue() {
        return 40;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.CHESTPLATE;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DEFENSE, 40D)
                .withBase(ItemStatistic.FARMING_FORTUNE, 5D)
                .build();
    }
}
