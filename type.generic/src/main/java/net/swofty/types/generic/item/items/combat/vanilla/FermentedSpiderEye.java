package net.swofty.types.generic.item.items.combat.vanilla;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FermentedSpiderEye implements CustomSkyBlockItem, Sellable, DefaultCraftable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.BROWN_MUSHROOM, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.SUGAR, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.SPIDER_EYE, 1));
        List<String> pattern = List.of(
                "AB",
                " C");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.FERMENTED_SPIDER_EYE, 1), ingredientMap, pattern);
    }

    @Override
    public double getSellValue() {
        return 10;
    }
}
