package net.swofty.types.generic.item.items.weapon.vanilla;

import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.ItemGroups;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchantable;
import net.swofty.types.generic.item.impl.ExtraRarityDisplay;

import java.util.List;

public class WoodenSword implements CustomSkyBlockItem, Enchantable, ExtraRarityDisplay {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 20)
                .build();
    }

    @Override
    public boolean showEnchantLores() {
        return true;
    }

    @Override
    public List<ItemGroups> getItemGroups() {
        return List.of(ItemGroups.SWORD);
    }

    @Override
    public String getExtraRarityDisplay() {
        return " SWORD";
    }
}
