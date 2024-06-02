package net.swofty.types.generic.item.items.communitycenter.sacks;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlowerSack implements CustomSkyBlockItem, SkullHead, NotFinishedYet {
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
                "§7§7Items: §aAllium§7, §aAzure Bluet§7, §aBlue Orchid§7, §aDandelion§7,",
                "§7§aEnchanted Dandelion§7, §aEnchanted Poppy§7, §aEndstone Rose§7,",
                "§7§aLilac§7, §aOrange Tulip§7, §aOxeye Daisy§7, §aPeony§7, §aPink Tulip§7, §aPoppy§7,",
                "§7§aRed Tulip§7, §aRose Bush§7, §aSunflower§7, §aWhite Tulip",
                "",
                "§7Capacity: §e20,160 per item",
                "§8Sacks sum their capacity.",
                "",
                "§eRight Click to open sack!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "afc3b5db9bd99cd12161ed2ea4623795f28e793c6dab67cd3e803ccfaaad7cfd";
    }
}
