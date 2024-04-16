package net.swofty.types.generic.item.items.armor.squire;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.LeatherColour;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class SquireBoots implements CustomSkyBlockItem, LeatherColour, StandardItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 20D)
                .withAdditive(ItemStatistic.DEFENSE, 20D)
                .build();
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.BOOTS;
    }

    @Override
    public Color getLeatherColour() {
        return new Color(84, 84, 84);
    }
}
