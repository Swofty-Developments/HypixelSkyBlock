package net.swofty.types.generic.item.items.armor.starlight;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.LeatherColour;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class StarlightBoots implements CustomSkyBlockItem, StandardItem, LeatherColour {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .with(ItemStatistic.HEALTH, 30D)
                .with(ItemStatistic.DEFENSE, 30D)
                .with(ItemStatistic.INTELLIGENCE, 50D)
                .build();
    }

    @Override
    public Color getLeatherColour() {
        return new Color(212, 0, 255);
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.BOOTS;
    }
}
