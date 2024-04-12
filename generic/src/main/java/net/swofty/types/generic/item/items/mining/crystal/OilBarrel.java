package net.swofty.types.generic.item.items.mining.crystal;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class OilBarrel implements CustomSkyBlockItem, SkullHead, ExtraUnderNameDisplay {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "d5d2750595477ecc13869580b12ffc3b13fc2b3ac3e5035ecfc9aafa036722a2";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Machine Fuel";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7Adds §2+10,000♢ Fuel §7to a",
                "§7refuelable machine.",
                "§7§7§o100% unrenewable energy!"));
    }
}
