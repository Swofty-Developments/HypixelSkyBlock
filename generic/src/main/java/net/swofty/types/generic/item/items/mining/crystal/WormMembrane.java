package net.swofty.types.generic.item.items.mining.crystal;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class WormMembrane implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
}
