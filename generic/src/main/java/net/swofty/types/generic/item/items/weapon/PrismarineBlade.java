package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrismarineBlade implements CustomSkyBlockItem, SwordImpl, Craftable, Sellable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 50D)
                .with(ItemStatistic.STRENGTH, 25D)
                .build();
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.PRISMARINE_SHARD, 32));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.STICK, 1));
        List<String> pattern = List.of(
                "A",
                "A",
                "B");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.PRISMARINE_BLADE), ingredientMap, pattern);
    }

    @Override
    public double getSellValue() {
        return 160;
    }
}
