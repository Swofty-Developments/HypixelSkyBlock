package net.swofty.types.generic.item.items.weapon;

import net.swofty.commons.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ArrowImpl;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.impl.Enchantable;
import net.swofty.types.generic.item.impl.Reforgable;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class FlintArrow implements CustomSkyBlockItem, ArrowImpl {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 1D)
                .build();
    }
}
