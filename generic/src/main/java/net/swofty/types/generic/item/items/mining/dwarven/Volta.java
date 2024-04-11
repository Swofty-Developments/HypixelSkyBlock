package net.swofty.types.generic.item.items.mining.dwarven;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class Volta implements CustomSkyBlockItem, Sellable, SkullHead, ExtraUnderNameDisplay {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7Adds §2+10,000♢ Fuel §7to a refuelable",
                "§7machine.",
                "",
                "§7§7§oBzzzt"));
    }

    @Override
    public double getSellValue() {
        return 1111;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "63a405fb286dbb32e9b3908f60948f0207306c825e63ac9e626ed1dbb2f7a2be";
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Machine Fuel";
    }
}
