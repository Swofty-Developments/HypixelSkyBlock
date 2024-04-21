package net.swofty.types.generic.item.items.end.armor.youngdragon;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YoungDragonHelmet implements CustomSkyBlockItem, StandardItem, SkullHead, DefaultCraftable, Unstackable, Sellable {

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.YOUNG_DRAGON_FRAGMENT, 10));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "AAA",
                "A A");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.COMBAT, new SkyBlockItem(ItemType.YOUNG_DRAGON_HELMET), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 70D)
                .withAdditive(ItemStatistic.DEFENSE, 110D)
                .withAdditive(ItemStatistic.SPEED, 20D)
                .build();
    }

    @Override
    public double getSellValue() {
        return 100000;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "ecf6ae4d827b77e33e4310c7fbc8683a81feb2b52ad1f2604c61d336c57a19c0";
    }
}