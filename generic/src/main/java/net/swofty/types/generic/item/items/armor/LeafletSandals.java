package net.swofty.types.generic.item.items.armor;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeafletSandals implements CustomSkyBlockItem, BootsImpl, LeatherColour, Sellable, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder().with(ItemStatistic.HEALTH, 15D).build();
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
                "L L");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.LEAFLET_SANDALS), ingredientMap, pattern);
    }
}
