package net.swofty.types.generic.item.items.communitycenter.sacks;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Sack;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DwarvenSack implements SkullHead, NotFinishedYet, Sack {

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Item pickups go directly into your",
                "§7sacks.",
                "",
                "§7§7Items: §aBiofuel§7, §aGlacite Jewel§7, §aGoblin Egg§7, §aOil Barrel§7,",
                "§7§aPlasma§7, §aSorrow§7, §aVolta§7, §a§3Blue Goblin Egg§7, §a§aGreen Goblin Egg§7,",
                "§7§a§cRed Goblin Egg§7, §a§eYellow Goblin Egg",
                "",
                "§7Capacity: §e20,160 per item",
                "§8Sacks sum their capacity.",
                "",
                "§eRight Click to open sack!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "daf9711d231e3abfb6fb744711f0d86e6041ee077dae16ddc703a7b4ea165d58";
    }

    @Override
    public List<ItemTypeLinker> getSackItems() {
        return List.of(
                //ItemTypeLinker.BIOFUEL,
                ItemTypeLinker.GLACITE_JEWEL,
                ItemTypeLinker.GOBLIN_EGG,
                ItemTypeLinker.OIL_BARREL,
                ItemTypeLinker.PLASMA,
                ItemTypeLinker.SORROW,
                ItemTypeLinker.VOLTA,
                ItemTypeLinker.BLUE_GOBLIN_EGG,
                ItemTypeLinker.GREEN_GOBLIN_EGG,
                ItemTypeLinker.RED_GOBLIN_EGG,
                ItemTypeLinker.YELLOW_GOBLIN_EGG
        );
    }


    @Override
    public int getMaximumCapacity() {
        return 20160;
    }
}
