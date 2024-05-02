package net.swofty.types.generic.item.items.armor.fancytuxedo;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.LeatherColour;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class FancyTuxedoBoots implements CustomSkyBlockItem, StandardItem, LeatherColour, CustomDisplayName {
    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Fancy Tuxedo Oxfords";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(51, 42, 42);
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.BOOTS;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.CRIT_DAMAGE, 35D)
                .withBase(ItemStatistic.SPEED, 10D)
                .withBase(ItemStatistic.INTELLIGENCE, 75D)
                .build();
    }
}
