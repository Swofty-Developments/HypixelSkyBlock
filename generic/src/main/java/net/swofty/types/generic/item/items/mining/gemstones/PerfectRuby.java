package net.swofty.types.generic.item.items.mining.gemstones;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Unstackable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class PerfectRuby implements CustomSkyBlockItem, Unstackable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "39b6e047d3b2bca85e8cc49e5480f9774d8a0eafe6dfa9559530590283715142";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A perfectly §crefined §cRuby§7.",
                "",
                "§7§7Some say that when §eharnessed",
                "§eproperly§7, it can give its",
                "§7owner extra §c❤ Health§7."));
    }
}
