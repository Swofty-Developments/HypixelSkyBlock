package net.swofty.types.generic.item.items.miscellaneous.decorations;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PinkBerry implements CustomSkyBlockItem, SkullHead {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§8Decoration item");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "778308fe4dda1bb8a96f5b226b32542f49fd65bc55b44bbc31343eb400cf5e2";
    }

    @Override
    public String getAbsoluteName(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "§fPink Berry";
    }

}
