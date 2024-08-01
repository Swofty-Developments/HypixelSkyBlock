package net.swofty.types.generic.item.items.mining.vanilla;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.swofty.commons.item.ItemType;

public class StonePickaxe implements CustomSkyBlockItem, PickaxeImpl, DefaultCraftable, Sellable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.MINING_SPEED, 110D)
                .withBase(ItemStatistic.DAMAGE, 20D)
                .withBase(ItemStatistic.BREAKING_POWER, 2D)
                .build();
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new ItemQuantifiable(ItemTypeLinker.COBBLESTONE, 1));
        ingredientMap.put('B', new ItemQuantifiable(ItemTypeLinker.STICK, 1));
        ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "AAA",
                " B ",
                " B ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.STONE_PICKAXE), ingredientMap, pattern);
    }

    @Override
    public double getSellValue() {
        return 1;
    }
}

