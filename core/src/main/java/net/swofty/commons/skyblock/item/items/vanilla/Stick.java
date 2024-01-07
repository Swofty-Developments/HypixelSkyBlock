package net.swofty.commons.skyblock.item.items.vanilla;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.Craftable;
import net.swofty.commons.skyblock.item.impl.CustomSkyBlockItem;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;
import net.swofty.commons.skyblock.item.impl.recipes.ShapelessRecipe;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;

public class Stick implements CustomSkyBlockItem, Craftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.STICK), 4)
                .add(ItemType.OAK_PLANKS, 2);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
