package net.swofty.types.generic.item.items.fishing.crimson;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class SilverMagmafish implements CustomSkyBlockItem, SkullHead, Sellable, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "353e10c0712ff28d877b9cef990d613b21d843732663f4aac385dc7db97ac54a";
    }

    @Override
    public double getSellValue() {
        return 1600;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                new SkyBlockItem(ItemType.SILVER_MAGMAFISH), 1)
                .add(ItemType.MAGMAFISH, 16)
                .add(ItemType.MAGMAFISH, 16)
                .add(ItemType.MAGMAFISH, 16)
                .add(ItemType.MAGMAFISH, 16)
                .add(ItemType.MAGMAFISH, 16);
    }
}
