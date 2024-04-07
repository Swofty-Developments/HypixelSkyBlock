package net.swofty.types.generic.item.items.crimson;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class GoldMagmafish implements CustomSkyBlockItem, SkullHead, Sellable, DefaultCraftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "5324eda6af85e50f35180607341728ff90faea1465be57a6242c6dea63aa3f28";
    }

    @Override
    public double getSellValue() {
        return 128000;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                new SkyBlockItem(ItemType.GOLD_MAGMAFISH), 1)
                .add(ItemType.SILVER_MAGMAFISH, 16)
                .add(ItemType.SILVER_MAGMAFISH, 16)
                .add(ItemType.SILVER_MAGMAFISH, 16)
                .add(ItemType.SILVER_MAGMAFISH, 16)
                .add(ItemType.SILVER_MAGMAFISH, 16);
    }
}