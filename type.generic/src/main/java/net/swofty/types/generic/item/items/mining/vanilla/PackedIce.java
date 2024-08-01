package net.swofty.types.generic.item.items.mining.vanilla;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.block.BlockType;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.PlaceableCustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackedIce implements PlaceableCustomSkyBlockItem, Sellable, DefaultCraftable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new ItemQuantifiable(ItemTypeLinker.ICE, 1));
        List<String> pattern = List.of(
                "AAA",
                "AAA",
                "AAA");
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.PACKED_ICE), ingredientMap, pattern);
    }

    @Override
    public double getSellValue() {
        return 4.5;
    }

    @Override
    public @Nullable BlockType getAssociatedBlockType() {
        return null;
    }
}
