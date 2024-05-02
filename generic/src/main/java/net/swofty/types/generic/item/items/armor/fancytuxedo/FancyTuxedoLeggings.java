package net.swofty.types.generic.item.items.armor.fancytuxedo;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.LeatherColour;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class FancyTuxedoLeggings implements CustomSkyBlockItem, StandardItem, LeatherColour, CustomDisplayName {

    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "Fancy Tuxedo Pants";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(212, 212, 212);
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.LEGGINGS;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.CRIT_DAMAGE, 35D)
                .withBase(ItemStatistic.INTELLIGENCE, 75D)
                .build();
    }
}
