package net.swofty.types.generic.item.items.combat.slayer.zombie;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class ScytheBlade implements CustomSkyBlockItem, Enchanted {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
