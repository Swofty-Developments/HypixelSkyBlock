package net.swofty.types.generic.item.items.armor;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class MinerOutfitHelmet implements CustomSkyBlockItem, StandardItem, LeatherColour, Sellable {

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .with(ItemStatistic.DEFENSE, 15D)
                .build();
    }

    @Override
    public Color getLeatherColour() {
        return new Color(0x2DE35E);
    }

    @Override
    public double getSellValue() {
        return 1920;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}
