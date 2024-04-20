package net.swofty.types.generic.item.items.mining;

import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.PickaxeImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RookiePickaxe implements CustomSkyBlockItem, PickaxeImpl {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
            .withAdditive(ItemStatistic.MINING_SPEED, 150D)
            .withAdditive(ItemStatistic.BREAKING_POWER, 2D)
            .withAdditive(ItemStatistic.DAMAGE, 15D)
            .build();
    }

    //still needs to start with efficiency

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
            "§7§oThe very first pickaxe",
            "§7§omodel! Invented by the famous",
            "§7§oThomas Pickson."
        ));
    }
}
