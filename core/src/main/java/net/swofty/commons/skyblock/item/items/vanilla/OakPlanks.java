package net.swofty.commons.skyblock.item.items.vanilla;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.Craftable;
import net.swofty.commons.skyblock.item.impl.CustomSkyBlockItem;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;
import net.swofty.commons.skyblock.item.impl.recipes.ShapelessRecipe;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;

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
