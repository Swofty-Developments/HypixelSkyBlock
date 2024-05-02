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

public class ElegantTuxedoChestplate implements CustomSkyBlockItem, StandardItem, LeatherColour, CustomDisplayName {
    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Elegant Tuxedo Jacket";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(25, 25, 25);
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.CHESTPLATE;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.CRIT_DAMAGE, 100D)
                .withBase(ItemStatistic.INTELLIGENCE, 300D)
                .build();
    }
}
