package net.swofty.item.items.armor;

import net.minestom.server.color.Color;
import net.swofty.item.ItemType;
import net.swofty.item.MaterialQuantifiable;
import net.swofty.item.ReforgeType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.*;
import net.swofty.item.impl.recipes.ShapedRecipe;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeafletTunic implements CustomSkyBlockItem, Reforgable, ExtraRarityDisplay, LeatherColour, Sellable, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder().with(ItemStatistic.HEALTH, 35).build();
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.ARMOR;
    }

    @Override
    public String getExtraRarityDisplay() {
        return " CHESTPLATE";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(0x2DE35E);
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
                "L L",
                "LLL",
                "LLL");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.LEAFLET_TUNIC), ingredientMap, pattern);
    }
}
