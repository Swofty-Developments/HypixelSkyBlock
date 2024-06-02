package net.swofty.types.generic.item.items.mining.vanilla;

import net.swofty.types.generic.block.BlockType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.MinionFuelItem;
import net.swofty.types.generic.item.impl.PlaceableCustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import javax.annotation.Nullable;
import java.util.List;

public class CoalBlock implements PlaceableCustomSkyBlockItem, Sellable, MinionFuelItem {
    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Increases the speed of",
                "§7your minion by §a5% §7for 5",
                "§7hours.");
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 18;
    }

    @Override
    public double getMinionFuelPercentage() {
        return 5;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 18000000; // 5 Hours
    }

    @Override
    public @Nullable BlockType getAssociatedBlockType() {
        return null;
    }
}
