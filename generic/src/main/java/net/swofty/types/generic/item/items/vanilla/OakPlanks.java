package net.swofty.types.generic.item.items.vanilla;

import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class OakPlanks implements CustomSkyBlockItem, Craftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.OAK_PLANKS), 4)
                .add(ItemType.OAK_LOG, 1);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
