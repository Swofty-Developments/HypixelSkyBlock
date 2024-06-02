package net.swofty.types.generic.item.items.minion.upgrade.fuel;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlasmaBucket implements CustomSkyBlockItem, Sellable, MinionFuelItem, Enchanted, DefaultCraftable {
    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Increases the speed of",
                "§7your minion by §a35%. §7Unlimited",
                "§7Duration!");
    }
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.MAGMA_BUCKET, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.HEAT_CORE, 1));
        List<String> pattern = List.of(
                "ABA");
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.PLASMA_BUCKET), ingredientMap, pattern);
    }

    @Override
    public double getMinionFuelPercentage() {
        return 35;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 0; // Infinite
    }

    @Override
    public double getSellValue() {
        return 50000;
    }
}
