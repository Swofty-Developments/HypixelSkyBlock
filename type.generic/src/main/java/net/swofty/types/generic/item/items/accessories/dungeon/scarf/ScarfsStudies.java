package net.swofty.types.generic.item.items.accessories.dungeon.scarf;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TieredTalisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScarfsStudies implements TieredTalisman, NotFinishedYet, SkullHead {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Gain dungeon class experience",
                "§a+2% faster.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "6de4ab129e137f9f4cbf7060318ee1748dc39da9b5d129a8da0e614e2337693";
    }

    @Override
    public ItemTypeLinker getBaseTalismanTier() {
        return ItemTypeLinker.SCARFS_STUDIES;
    }

    @Override
    public Integer getTier() {
        return 1;
    }
}
