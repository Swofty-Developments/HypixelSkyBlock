package net.swofty.types.generic.item.items.communitycenter.katitems;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class KatBouquet implements CustomSkyBlockItem, Enchanted, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(List.of(
                "§7Give this to §bKat §7the §aPet Sitter §7in",
                "§7order to skip §95 days §7of wait time",
                "§7while upgrading your pet!",
                "§7",
                "§7§eRight-click on §bKat §eto use!"
        ));
    }
}
