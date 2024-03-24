package net.swofty.types.generic.item.items.combat.slayer.enderman;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class NullSphere implements CustomSkyBlockItem, Enchanted {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
