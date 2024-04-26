package net.swofty.types.generic.item.items.combat.slayer.enderman.drops;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class EtherwarpMerger implements CustomSkyBlockItem, SkullHead, Sellable, TrackedUniqueItem, RightClickRecipe {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 100000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3e5314f4919691ccbf807743dae47ae45ac2e3ff08f79eecdd452fe602eff7f6";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "",
                "§7§8Crafted by the Etherchemist",
                "§8and dropped by the Voidgloom",
                "§8Seraph."));
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.ETHERWARP_CONDUIT;
    }
}
