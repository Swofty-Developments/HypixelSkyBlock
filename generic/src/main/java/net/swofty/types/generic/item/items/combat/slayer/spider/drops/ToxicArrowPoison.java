package net.swofty.types.generic.item.items.combat.slayer.spider.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class ToxicArrowPoison implements CustomSkyBlockItem, Sellable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 2000;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§8Consumed on arrow shot",
                "§7Arrows deal an additional",
                "§7§210% §7of damage as poison",
                "§7and reduce healing by §224%",
                "§2§7over §b4 §7seconds."));
    }
}
