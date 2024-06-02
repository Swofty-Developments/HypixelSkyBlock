package net.swofty.types.generic.item.items.minion.upgrade.fuel;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MinionFuelItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import javax.annotation.Nullable;
import java.util.List;

public class EnchantedLavaBucket  implements CustomSkyBlockItem, Sellable, MinionFuelItem, Enchanted {
    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Increases the speed of",
                "§7your minion by §a25%. §7Unlimited",
                "§7Duration!");
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getMinionFuelPercentage() {
        return 25;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 0; // Infinite
    }

    @Override
    public double getSellValue() {
        return 50000;
    }
}
