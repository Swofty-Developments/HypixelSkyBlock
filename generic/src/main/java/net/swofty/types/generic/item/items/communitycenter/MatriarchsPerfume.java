package net.swofty.types.generic.item.items.communitycenter;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MatriarchsPerfume implements CustomSkyBlockItem, CustomDisplayName, Enchanted, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Consume to apply a pungent smell",
                "§7upon yourself, confusing §6§lThe",
                "§6§lMatriarch §7in the §cCrimson Isle §7into",
                "§7releasing new pearls.",
                "",
                "§8Can be used up to twice daily!",
                "",
                "§eRight-click to consume!");
    }
    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Matriarch's Perfume";
    }
}
