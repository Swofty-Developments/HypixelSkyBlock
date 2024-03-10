package net.swofty.types.generic.item.items.mining.gemstones;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class FineTopaz implements CustomSkyBlockItem, Sellable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 19200;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "92cb6e51c461e7359526bea5e06209cddde7c6469a819f3405cf0a038c159502";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A type of §eTopaz §7that has",
                "§7clearly been treated with care.",
                "",
                "§7§7Some say that when §eharnessed",
                "§eproperly§7, it can imbue an item",
                "§7with the Pristine §7enchantment."));
    }
}
