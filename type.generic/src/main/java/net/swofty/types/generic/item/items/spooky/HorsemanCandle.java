package net.swofty.types.generic.item.items.spooky;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class HorsemanCandle implements CustomSkyBlockItem, SkullHead, CustomDisplayName, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "e521cf45ebe321e4860d76fd7b6abd379993c6f2c9d4a19c37bdcb7b11b6b";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Used to summon the Headless",
                "§7Horseman. This item can only be",
                "§7used at night!",
                "",
                "§7Can be spawned in:",
                "§cGraveyard",
                "§2Wilderness"));
    }

    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "Horseman's Candle";
    }
}
