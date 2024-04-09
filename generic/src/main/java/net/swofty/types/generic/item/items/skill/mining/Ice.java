package net.swofty.types.generic.item.items.skill.mining;

import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class Ice implements CustomSkyBlockItem, Sellable {

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 1;
    }
}
