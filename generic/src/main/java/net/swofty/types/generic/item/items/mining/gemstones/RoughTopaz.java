package net.swofty.types.generic.item.items.mining.gemstones;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class RoughTopaz implements CustomSkyBlockItem, Sellable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 3;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3fd960722ec29c66716ae5ca97b9b6b2628984e1d6f9d2592cd089914206a1b";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Harvested from the plentiful",
                "§7gemstone veins in the §5Crystal",
                "§5Hollows§7.",
                "",
                "§7§7Some say that when §eharnessed",
                "§eproperly§7, it can give its",
                "§7owner extra §5✧ Pristine§7."));
    }
}
