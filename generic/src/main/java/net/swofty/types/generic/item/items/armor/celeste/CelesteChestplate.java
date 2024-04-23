package net.swofty.types.generic.item.items.armor.celeste;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.LeatherColour;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class CelesteChestplate implements CustomSkyBlockItem, StandardItem, LeatherColour, Sellable {
    @Override
    public Color getLeatherColour() {
        return new Color(255, 142, 222);
    }

    @Override
    public double getSellValue() {
        return 4000;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.CHESTPLATE;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 20D)
                .withAdditive(ItemStatistic.DEFENSE, 20D)
                .withAdditive(ItemStatistic.INTELLIGENCE, 60D)
                .build();
    }
}
