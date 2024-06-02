package net.swofty.types.generic.item.items.communitycenter.sacks;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PocketSackInASack implements CustomSkyBlockItem, SkullHead, CustomDisplayName, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Combine this item in an anvil with any",
                "§7sack to increase its capacity by",
                "§7§c+200% §7its §coriginal §7capacity.",
                "",
                "§7This item can be combined with a sack",
                "§7up to three times!",
                "",
                "§7§8It's a pocket sack, to be stitched",
                "§8inside another sack!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "7442c66f4bf9aa4256fa7b49c6367d4658408ec408477879ac8076794402d95b";
    }

    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Pocket Sack-in-a-Sack";
    }
}
