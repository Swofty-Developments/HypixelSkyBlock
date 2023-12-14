package net.swofty.item.items.weapon;

import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.ItemStatistic;
import net.swofty.item.impl.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class Hyperion implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 100)
                .with(ItemStatistic.HEALTH, 20)
                .with(ItemStatistic.DEFENSE, 30)
                .build();
    }

    @Override
    public ArrayList<String> getLore() {
        return new ArrayList<>(Arrays.asList("This item literally comes", "out of your mum and", "says §aHELLO §7lmao."));
    }
}
