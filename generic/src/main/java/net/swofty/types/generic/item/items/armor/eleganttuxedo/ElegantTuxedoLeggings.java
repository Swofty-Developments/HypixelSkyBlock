package net.swofty.types.generic.item.items.armor.eleganttuxedo;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.LeatherColour;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ElegantTuxedoLeggings implements CustomSkyBlockItem, StandardItem, LeatherColour, CustomDisplayName {
    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Elegant Tuxedo Pants";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(254, 253, 252);
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.LEGGINGS;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.CRIT_DAMAGE, 50D)
                .withBase(ItemStatistic.INTELLIGENCE, 100D)
                .build();
    }
}
