package net.swofty.types.generic.item.items.crimson;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class DiamondMagmafish implements CustomSkyBlockItem, SkullHead, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "19b393eb6a5bd65d735aaa3b3cfa993b50f5e536d7a13b535514bd0740d63350";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FISHING,
                new SkyBlockItem(ItemType.DIAMOND_MAGMAFISH), 1)
                .add(ItemType.GOLD_MAGMAFISH, 16)
                .add(ItemType.GOLD_MAGMAFISH, 16)
                .add(ItemType.GOLD_MAGMAFISH, 16)
                .add(ItemType.GOLD_MAGMAFISH, 16)
                .add(ItemType.GOLD_MAGMAFISH, 16);
    }
}