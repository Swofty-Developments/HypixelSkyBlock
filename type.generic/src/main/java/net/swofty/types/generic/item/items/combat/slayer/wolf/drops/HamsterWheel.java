package net.swofty.types.generic.item.items.combat.slayer.wolf.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MinionFuelItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class HamsterWheel implements CustomSkyBlockItem, Sellable, Enchanted, MinionFuelItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 20000;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7increases the speed of",
                "§7your minion by §a50%§7",
                "§7for 24 hours§7!"));
    }

    @Override
    public double getMinionFuelPercentage() {
        return 50;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 86400000;
    }
}
