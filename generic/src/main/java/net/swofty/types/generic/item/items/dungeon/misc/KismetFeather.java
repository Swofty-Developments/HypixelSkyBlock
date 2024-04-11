package net.swofty.types.generic.item.items.dungeon.misc;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KismetFeather implements CustomSkyBlockItem, Enchanted {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Allows you to reroll a",
                "§cdungeon §7reward chest.",
                "",
                "§7Keep a §9feather §7in your",
                "§7inventory and open a reward",
                "§7chest to use.",
                "",
                "§aYou may only use a single",
                "§afeather per dungeon run!",
                "",
                "§8When life hands you a",
                "§8lemon, reroll it."
        ));
    }
}
