package net.swofty.types.generic.item.items.combat.slayer.wolf;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Unstackable;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class OverfluxCapacitor implements CustomSkyBlockItem, Enchanted, Unstackable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
