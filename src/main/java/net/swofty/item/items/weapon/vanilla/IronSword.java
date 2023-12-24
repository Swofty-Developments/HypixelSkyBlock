package net.swofty.item.items.weapon.vanilla;

import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.Enchantable;
import net.swofty.item.impl.ExtraRarityDisplay;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.ItemGroups;

import java.util.List;

public class IronSword implements CustomSkyBlockItem, Enchantable, ExtraRarityDisplay {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 30)
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

