package net.swofty.types.generic.item.items.miscellaneous;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.Arrays;
import java.util.List;

public class KatFlower implements CustomSkyBlockItem, Enchanted {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
    @Override
    public List<String> getAbsoluteLore(SkyBlockPlayer player, SkyBlockItem item) {
        return Arrays.asList(
                "§7Give this Kat the Pet Sitter",
                "§7in order to skip §91 day §7of",
                "§7wait time while upgrading your",
                "§7pet!",
                "",
                "§eRight-click on Kat to use."
        );
    }
}
