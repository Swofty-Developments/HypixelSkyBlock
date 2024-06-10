package net.swofty.types.generic.item.items.minion.upgrade.fuel;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistics;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagmaBucket  implements CustomSkyBlockItem, Sellable, MinionFuelItem, Enchanted, DefaultCraftable {
    @Override
    public List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Increases the speed of",
                "§7your minion by §a30%. §7Unlimited",
                "§7Duration!");
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_LAVA_BUCKET, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.HEAT_CORE, 1));
        List<String> pattern = List.of(
                "ABA");
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.MAGMA_BUCKET), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getMinionFuelPercentage() {
        return 30;
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
