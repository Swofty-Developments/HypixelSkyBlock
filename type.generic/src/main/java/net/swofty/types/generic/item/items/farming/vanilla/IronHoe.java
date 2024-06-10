package net.swofty.types.generic.item.items.farming.vanilla;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IronHoe implements CustomSkyBlockItem, StandardItem, DefaultCraftable, Sellable {

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.IRON_INGOT, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.STICK, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemTypeLinker.AIR, 1));
        List<String> pattern = List.of(
                "AA",
                " B",
                " B");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.IRON_HOE), ingredientMap, pattern);
    }

    @Override
    public double getSellValue() {
        return 3;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HOE;
    }
}
