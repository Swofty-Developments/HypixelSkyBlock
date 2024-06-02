package net.swofty.types.generic.item.items.communitycenter.upgradecomponents;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MinionStorageExpander implements CustomSkyBlockItem, SkullHead, CustomDisplayName {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Craft with minion §9Large Storage",
                "§9§7chests to upgrade their size to §a21",
                "§a§7and §a27 §7slots.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f37cae5c51eb1558ea828f58e0dff8e6b7b0b1a183d737eecf714661761";
    }

    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Minion Storage X-pender";
    }
}
