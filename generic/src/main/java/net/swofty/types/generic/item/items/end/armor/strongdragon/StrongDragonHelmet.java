package net.swofty.types.generic.item.items.end.armor.strongdragon;

import net.minestom.server.color.Color;
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

public class StrongDragonHelmet implements CustomSkyBlockItem, StandardItem, SkullHead, DefaultCraftable, Unstackable, Sellable {

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.STRONG_DRAGON_FRAGMENT, 10));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "AAA",
                "A A");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.COMBAT, new SkyBlockItem(ItemType.STRONG_DRAGON_HELMET), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 70D)
                .withAdditive(ItemStatistic.DEFENSE, 110D)
                .withAdditive(ItemStatistic.STRENGTH, 25D)
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
        return "efde09603b0225b9d24a73a0d3f3e3af29058d448ccd7ce5c67cd02fab0ff682";
    }
}