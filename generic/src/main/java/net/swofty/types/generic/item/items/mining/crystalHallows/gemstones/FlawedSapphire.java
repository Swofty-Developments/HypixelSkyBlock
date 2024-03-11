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

public class FlawedSapphire implements CustomSkyBlockItem, Sellable, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 240;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8a0af99e8d8703194a847a55268cf5ef4ac4eb3b24c0ed86551339d10b646529";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A slightly better version of",
                "§7§bSapphire§7, but it could still",
                "§7use some work.",
                "",
                "§7§7Some say that when §eharnessed",
                "§eproperly§7, it can give its",
                "§7owner extra §b✎ Intelligence§7."));
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FLAWED_SAPPHIRE_GEM), 1)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16);
    }
}
