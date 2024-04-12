package net.swofty.types.generic.item.items.combat.slayer.enderman.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class TwilightArrowPoison implements CustomSkyBlockItem, Sellable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 650;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§8Consumed on arrow shot",
                "§7Arrows apply a §5ᛤ §5poison",
                "§5§7that increases all incoming",
                "§7damage by §c1.1x §7for §a20s§7."));
    }
}
