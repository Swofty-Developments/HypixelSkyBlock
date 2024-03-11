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

public class FineRuby implements CustomSkyBlockItem, Sellable, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public double getSellValue() {
        return 19200;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "e672959028f274b379d430f068f0f15a4f793eac12afb94ae0b4e50cf895df0f";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A type of §cRuby §7that has",
                "§7clearly been treated with care.",
                "",
                "§7§7Some say that when §eharnessed",
                "§eproperly§7, it can give its",
                "§7owner extra §c❤ Health§7."));
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FINE_RUBY_GEM), 1)
                .add(ItemType.FLAWED_RUBY_GEM, 16)
                .add(ItemType.FLAWED_RUBY_GEM, 16)
                .add(ItemType.FLAWED_RUBY_GEM, 16)
                .add(ItemType.FLAWED_RUBY_GEM, 16)
                .add(ItemType.FLAWED_RUBY_GEM, 16);
    }
}
