package net.swofty.types.generic.item.items.mining.crystal;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.PickaxeImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class Pickonimbus2000 implements CustomSkyBlockItem, PickaxeImpl, Sellable, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 30D)
                .withBase(ItemStatistic.MINING_SPEED, 1500D)
                .withBase(ItemStatistic.BREAKING_POWER, 7D)
                .build();
    }

    @Override
    public double getSellValue() {
        return 12;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A powerful tool that breaks",
                "§7after §a5000§7 uses."));
    }
}
