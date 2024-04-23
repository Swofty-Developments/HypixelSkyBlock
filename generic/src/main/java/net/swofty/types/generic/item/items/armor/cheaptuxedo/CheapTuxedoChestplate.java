package net.swofty.types.generic.item.items.armor.cheaptuxedo;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class CheapTuxedoChestplate implements CustomSkyBlockItem, StandardItem, LeatherColour, NotFinishedYet, CustomDisplayName {
    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Cheap Tuxedo Jacket";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(56, 56, 56);
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.CHESTPLATE;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.CRIT_DAMAGE, 50D)
                .withAdditive(ItemStatistic.INTELLIGENCE, 50D)
                .build();
    }
}
