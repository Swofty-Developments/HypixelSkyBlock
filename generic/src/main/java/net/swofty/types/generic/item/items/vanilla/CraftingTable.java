package net.swofty.types.generic.item.items.vanilla;

import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingTable implements CustomSkyBlockItem, Craftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('P', new MaterialQuantifiable(ItemType.OAK_PLANKS, 1));
        List<String> pattern = List.of(
                "PP",
                "PP");
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE,
                new SkyBlockItem(ItemType.CRAFTING_TABLE), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
