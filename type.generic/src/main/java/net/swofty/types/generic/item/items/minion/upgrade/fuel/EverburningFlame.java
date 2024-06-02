package net.swofty.types.generic.item.items.minion.upgrade.fuel;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import javax.annotation.Nullable;
import java.util.List;

public class EverburningFlame implements CustomSkyBlockItem, SkullHead, Sellable, MinionFuelItem, NotFinishedYet {
    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Increases the speed of",
                "§7your minion by §a25%. §7Unlimited",
                "§7Duration!",
                "",
                "§6Everburning Bonus§7: §a5%§7 extra speed on Combat minions.");
    }

    @Override
    public String getSkullTexture(@org.jetbrains.annotations.Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "990cbd72e41a9bd411be929b73fd269206368b2810d6c6819918cb8eb66224f4";
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getMinionFuelPercentage() {
        return 35;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 0; // Infinite
    }

    @Override
    public double getSellValue() {
        return 159855;
    }
}
