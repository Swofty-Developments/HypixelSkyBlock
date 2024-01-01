package net.swofty.item.items.vanilla;

import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.Craftable;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.item.impl.recipes.ShapelessRecipe;
import net.swofty.user.statistics.ItemStatistics;

public class CraftingTable implements CustomSkyBlockItem, Craftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.CRAFTING_TABLE), 1)
                .add(ItemType.OAK_PLANKS, 4);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
