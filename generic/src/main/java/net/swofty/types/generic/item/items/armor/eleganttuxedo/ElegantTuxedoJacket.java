package net.swofty.types.generic.item.items.armor.eleganttuxedo;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.LeatherColour;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class ElegantTuxedoJacket implements CustomSkyBlockItem, StandardItem, LeatherColour, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.CRIT_DAMAGE, 100D)
                .withAdditive(ItemStatistic.INTELLIGENCE, 300D)
                .build();
    }

    @Override
    public Color getLeatherColour() {
        return new Color(25, 25, 25);
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.CHESTPLATE;
    }
}
