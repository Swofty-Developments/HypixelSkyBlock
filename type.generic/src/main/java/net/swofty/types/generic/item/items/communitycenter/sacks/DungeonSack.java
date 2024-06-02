package net.swofty.types.generic.item.items.communitycenter.sacks;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DungeonSack implements CustomSkyBlockItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Item pickups go directly into your",
                "§7sacks.",
                "",
                "§7§7Items: §aAncient Rose§7, §aBigfoot's Lasso§7, §aBonzo Fragment§7,",
                "§7§aDecoy§7, §aDungeon Chest Key§7, §aFel Pearl§7, §aHealing Tissue§7,",
                "§7§aInflatable Jerry§7, §aJolly Pink Rock§7, §aKismet Feather§7,",
                "§7§aL.A.S.R.'s Eye§7, §aLivid Fragment§7, §aMimic Fragment§7, §aScarf",
                "§aFragment§7, §aSpirit Leap§7, §aSuperboom TNT§7, §aThorn Fragment§7,",
                "§7§aTrap§7, §aWither Catalyst",
                "",
                "§7Capacity: §e20,160 per item",
                "§8Sacks sum their capacity.",
                "",
                "§eRight Click to open sack!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "fb96c585ccd35f073da38d165cb9bb18ff136f1a184eee3f44725354640ebbd4";
    }
}
