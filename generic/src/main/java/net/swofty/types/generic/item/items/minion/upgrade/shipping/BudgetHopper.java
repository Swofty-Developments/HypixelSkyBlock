package net.swofty.types.generic.item.items.minion.upgrade.shipping;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.MinionShippingItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class BudgetHopper implements CustomSkyBlockItem, MinionShippingItem, Sellable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7This item can be placed inside any",
                "§7minion. Automatically sells generated",
                "§7items when the minion has no space.",
                "§7Items are sold for §a" + getPercentageOfOriginalPrice() + "% §7of their",
                "§7selling price.");
    }

    @Override
    public double getPercentageOfOriginalPrice() {
        return 50;
    }

    @Override
    public double getSellValue() {
        return 1200;
    }
}
