package net.swofty.types.generic.item.items.minion;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class CobblestoneMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.COBBLESTONE;
    }
}
