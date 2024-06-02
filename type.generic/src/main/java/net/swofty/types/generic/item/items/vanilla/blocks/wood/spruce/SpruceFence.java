package net.swofty.types.generic.item.items.vanilla.blocks.wood.spruce;

import net.swofty.types.generic.block.BlockType;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.PlaceableCustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpruceFence implements PlaceableCustomSkyBlockItem, DefaultCraftable, Sellable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.SPRUCE_PLANKS, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.STICK, 1));
        List<String> pattern = List.of(
                "ABA",
                "ABA");
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.SPRUCE_FENCE, 3), ingredientMap, pattern);
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
