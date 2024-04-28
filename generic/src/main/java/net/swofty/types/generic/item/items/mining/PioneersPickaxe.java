package net.swofty.types.generic.item.items.mining;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.PickaxeImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class PioneersPickaxe implements CustomSkyBlockItem, PickaxeImpl {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.MINING_SPEED, 1D)
                .withBase(ItemStatistic.BREAKING_POWER, 1D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§oThe very first pickaxe",
                "§7§omodel! Invented by the famous",
                "§7§oThomas Pickson."
        ));
    }
}
