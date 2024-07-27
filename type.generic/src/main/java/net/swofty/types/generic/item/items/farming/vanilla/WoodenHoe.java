package net.swofty.types.generic.item.items.farming.vanilla;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.swofty.commons.item.ItemType;

public class WoodenHoe implements CustomSkyBlockItem, StandardItem, DefaultCraftable, Sellable {

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new ItemQuantifiable(ItemTypeLinker.OAK_PLANKS, 1));
        ingredientMap.put('B', new ItemQuantifiable(ItemTypeLinker.STICK, 1));
        ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "AA",
                " B",
                " B");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.WOODEN_HOE), ingredientMap, pattern);
    }

    @Override
    public double getSellValue() {
        return 1;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HOE;
    }
}
