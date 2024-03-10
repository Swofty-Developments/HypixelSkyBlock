package net.swofty.types.generic.item.items.mining.gemstones;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Unstackable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class FlawlessTopaz implements CustomSkyBlockItem, Unstackable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "d10964f3c479ad7d9afaf68a42cab7c107d2d884f575cae2f070ec6f935b3be";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A near perfect version of",
                "§7§eTopaz§7.",
                "",
                "§7§7Some say that when §eharnessed",
                "§eproperly§7, it can imbue an item",
                "§7with the Pristine §7enchantment."));
    }
}
