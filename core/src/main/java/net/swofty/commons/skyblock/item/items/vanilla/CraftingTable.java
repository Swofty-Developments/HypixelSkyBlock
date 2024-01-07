package net.swofty.commons.skyblock.item.items.vanilla;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.MaterialQuantifiable;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.Craftable;
import net.swofty.commons.skyblock.item.impl.CustomSkyBlockItem;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;
import net.swofty.commons.skyblock.item.impl.recipes.ShapedRecipe;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;

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

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.CRAFTING_TABLE), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
}
