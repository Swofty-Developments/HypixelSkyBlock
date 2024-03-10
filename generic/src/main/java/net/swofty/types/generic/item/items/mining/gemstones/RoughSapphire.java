package net.swofty.types.generic.item.items.mining.gemstones;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class RoughSapphire implements CustomSkyBlockItem, Sellable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 3;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "cfcebe54dbc345ea7e22206f703e6b33befbe95b6a918bd1754b76188bc65bb5";
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
                "§7owner extra §b✎ Intelligence§7."));
    }
}
