package net.swofty.item.items.mining.vanilla;

import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.Enchantable;
import net.swofty.item.impl.ExtraRarityDisplay;
import net.swofty.item.impl.MiningTool;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.ItemGroups;

import java.util.List;

public class IronPickaxe implements CustomSkyBlockItem, MiningTool, ExtraRarityDisplay, Enchantable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.MINING_SPEED, 160)
                .with(ItemStatistic.DAMAGE, 24)
                .build();
    }

    @Override
    public int getBreakingPower() {
        return 3;
    }

    @Override
    public String getExtraRarityDisplay() {
        return " PICKAXE";
    }

    @Override
    public boolean showEnchantLores() {
        return true;
    }

    @Override
    public List<ItemGroups> getItemGroups() {
        return List.of(ItemGroups.PICKAXE, ItemGroups.TOOLS);
    }
}
