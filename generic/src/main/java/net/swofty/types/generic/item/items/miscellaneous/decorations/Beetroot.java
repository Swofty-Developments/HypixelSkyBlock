package net.swofty.types.generic.item.items.miscellaneous.decorations;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Beetroot implements CustomSkyBlockItem, SkullHead {
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
        return "18f36ea228c4fd9afed5add6d0526de71b7ac0559eabfc2f60de6c4aa733f5";
    }

    @Override
    public String getAbsoluteName(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "§fBeetroot";
    }

}
