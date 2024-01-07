package net.swofty.commons.skyblock.item.items.mining.vanilla;

import net.swofty.commons.skyblock.item.ReforgeType;
import net.swofty.commons.skyblock.item.impl.*;
import net.swofty.commons.skyblock.user.statistics.ItemStatistic;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;
import net.swofty.commons.skyblock.utility.ItemGroups;

import java.util.List;

public class IronPickaxe implements CustomSkyBlockItem, MiningTool, ExtraRarityDisplay, Enchantable, Reforgable {
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

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.PICKAXES;
    }
}
