package net.swofty.item.items.vanilla;

import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.Craftable;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.item.impl.recipes.ShapelessRecipe;
import net.swofty.user.statistics.ItemStatistics;

public class OakPlanks implements CustomSkyBlockItem, Craftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(new SkyBlockItem(ItemType.OAK_PLANKS), 4)
                .add(ItemType.OAK_LOG, 1);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
