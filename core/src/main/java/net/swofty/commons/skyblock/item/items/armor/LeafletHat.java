package net.swofty.commons.skyblock.item.items.armor;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.MaterialQuantifiable;
import net.swofty.commons.skyblock.item.ReforgeType;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.*;
import net.swofty.commons.skyblock.item.impl.recipes.ShapedRecipe;
import net.swofty.commons.skyblock.user.statistics.ItemStatistic;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeafletHat implements CustomSkyBlockItem, Reforgable, ExtraRarityDisplay, Sellable, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder().with(ItemStatistic.HEALTH, 20).build();
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.ARMOR;
    }

    @Override
    public String getExtraRarityDisplay() {
        return " HELMET";
    }

    @Override
    public double getSellValue() {
        return 10;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('L', new MaterialQuantifiable(ItemType.OAK_LEAVES, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "LLL",
                "L L");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.LEAFLET_HAT), ingredientMap, pattern);
    }
}
