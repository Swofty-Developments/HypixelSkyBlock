package net.swofty.types.generic.item.items.end.armor.youngdragon;

import net.minestom.server.color.Color;
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

public class YoungDragonLeggings implements CustomSkyBlockItem, StandardItem, DefaultCraftable, LeatherColour, Sellable {

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.YOUNG_DRAGON_FRAGMENT, 10));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "AAA",
                "A A",
                "A A");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.COMBAT, new SkyBlockItem(ItemType.YOUNG_DRAGON_LEGGINGS), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 100D)
                .withAdditive(ItemStatistic.DEFENSE, 140D)
                .withAdditive(ItemStatistic.SPEED, 20D)
                .build();
    }

    @Override
    public Color getLeatherColour() {
        return new Color(221, 228, 240);
    }

    @Override
    public double getSellValue() {
        return 100000;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.LEGGINGS;
    }
}