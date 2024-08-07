package net.swofty.types.generic.item.items.vanilla.blocks.stone.quartz;

import net.swofty.types.generic.block.BlockType;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.MultiDefaultCraftable;
import net.swofty.types.generic.item.impl.PlaceableCustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuartzSlab implements PlaceableCustomSkyBlockItem, MultiDefaultCraftable, Sellable {
    @Override
    public List<SkyBlockRecipe<?>> getRecipes() {
        List<SkyBlockRecipe<?>> recipes = new ArrayList<>();

        Map<Character, ItemQuantifiable> ingredientMap1 = new HashMap<>();
        ingredientMap1.put('A', new ItemQuantifiable(ItemTypeLinker.CHISELED_QUARTZ_BLOCK, 1));
        List<String> pattern1 = List.of(
                "AAA");
        recipes.add(new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.QUARTZ_SLAB, 6), ingredientMap1, pattern1));

        Map<Character, ItemQuantifiable> ingredientMap2 = new HashMap<>();
        ingredientMap2.put('A', new ItemQuantifiable(ItemTypeLinker.QUARTZ_BLOCK, 1));
        List<String> pattern2 = List.of(
                "AAA");
        recipes.add(new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.QUARTZ_SLAB, 6), ingredientMap2, pattern2));
        return recipes;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public @Nullable BlockType getAssociatedBlockType() {
        return null;
    }

    @Override
    public double getSellValue() {
        return 1;
    }
}
