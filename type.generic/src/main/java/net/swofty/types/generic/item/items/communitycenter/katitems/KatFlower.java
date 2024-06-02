package net.swofty.types.generic.item.items.communitycenter.katitems;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class KatFlower implements CustomSkyBlockItem, Enchanted, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(List.of(
                "§7Give this Kat the Pet Sitter",
                "§7in order to skip §91 day §7of",
                "§7wait time while upgrading your",
                "§7pet!",
                "",
                "§eRight-click on Kat to use."
        ));
    }
}
