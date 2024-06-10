package net.swofty.types.generic.item.items.vanilla.items.dyes;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.MultiDefaultCraftable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;
import net.swofty.commons.item.ItemType;

public class CyanDye implements CustomSkyBlockItem, MultiDefaultCraftable {
    @Override
    public List<SkyBlockRecipe<?>> getRecipes() {
        List<SkyBlockRecipe<?>> recipes = new ArrayList<>();

        recipes.add(new ShapelessRecipe(SkyBlockRecipe.RecipeType.NONE,
                new SkyBlockItem(ItemTypeLinker.CYAN_DYE), 2)
                .add(ItemType.PITCHER_PLANT, 1));

        recipes.add(new ShapelessRecipe(SkyBlockRecipe.RecipeType.NONE,
                new SkyBlockItem(ItemTypeLinker.CYAN_DYE), 2)
                .add(ItemTypeLinker.BLUE_DYE, 1)
                .add(ItemTypeLinker.CACTUS_GREEN, 1));

        return recipes;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
}
