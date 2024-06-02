package net.swofty.types.generic.item.items.armor.starlight;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.LeatherColour;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class StarlightBoots implements CustomSkyBlockItem, StandardItem, LeatherColour, Sellable {
    @Override
    public Color getLeatherColour() {
        return new Color(212, 0, 255);
    }

    @Override
    public double getSellValue() {
        return 15000;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.BOOTS;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 30D)
                .withBase(ItemStatistic.DEFENSE, 30D)
                .withBase(ItemStatistic.INTELLIGENCE, 50D)
                .build();
    }
}
