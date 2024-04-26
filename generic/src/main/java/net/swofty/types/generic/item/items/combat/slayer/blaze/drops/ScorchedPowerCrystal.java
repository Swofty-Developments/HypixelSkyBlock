package net.swofty.types.generic.item.items.combat.slayer.blaze.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class ScorchedPowerCrystal implements CustomSkyBlockItem, SkullHead, Sellable, TrackedUniqueItem, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 350000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f099d4e25a569e2d018bd8d7e1a40ad2ccbc9bd1fd2ed48688bcc3f62a96213a";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7Powers §bBeacons §7for up to",
                "§7§348 hours §7at a time!",
                "",
                "§7Adds §6+1% §7minion speed buff and",
                "§7§a+20% §7profile stats to beacons",
                "§7fueled by this power crystal!",
                "",
                "§7Has a single §7use and can be stacked."));
    }
}
