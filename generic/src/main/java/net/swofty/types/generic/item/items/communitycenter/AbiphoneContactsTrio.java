package net.swofty.types.generic.item.items.communitycenter;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class AbiphoneContactsTrio implements CustomSkyBlockItem, Enchanted, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Adds §b+3 §7contact slots to",
                "§7your Abiphone plan, regardless",
                "§7of the model you use!",
                "",
                "§7§8Up to 12 of these may be used.",
                "",
                "§eRight-click to consume!");
    }
}
