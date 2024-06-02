package net.swofty.types.generic.item.items.communitycenter.stackingenchants;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class Expertise implements CustomSkyBlockItem, Enchanted, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§9Expertise I",
                "§7Grants §3+0.6α Sea Creature",
                "§3Chance §7and §3+2☯ Fishing",
                "§3Wisdom §7when killing Sea",
                "§7Creatures.");
    }
}
