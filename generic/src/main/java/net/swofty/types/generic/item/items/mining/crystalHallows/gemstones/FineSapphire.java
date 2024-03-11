package net.swofty.types.generic.item.items.mining.crystalHallows.gemstones;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class FineSapphire implements CustomSkyBlockItem, Sellable, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 19200;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "36161daa3589ec9c8187459ac36fd4dd2646c040678d3bfacb72a2210c6c801c";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A type of §bSapphire §7that has",
                "§7clearly been treated with care.",
                "",
                "§7§7Some say that when §eharnessed",
                "§eproperly§7, it can give its",
                "§7owner extra §b✎ Intelligence§7."));
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FINE_SAPPHIRE_GEM), 1)
                .add(ItemType.FLAWED_SAPPHIRE_GEM, 16)
                .add(ItemType.FLAWED_SAPPHIRE_GEM, 16)
                .add(ItemType.FLAWED_SAPPHIRE_GEM, 16)
                .add(ItemType.FLAWED_SAPPHIRE_GEM, 16)
                .add(ItemType.FLAWED_SAPPHIRE_GEM, 16);
    }
}
