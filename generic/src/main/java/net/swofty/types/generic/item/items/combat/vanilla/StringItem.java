package net.swofty.types.generic.item.items.combat.vanilla;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class StringItem implements CustomSkyBlockItem, Sellable, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 3;
    }
    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "String";
    }
}
