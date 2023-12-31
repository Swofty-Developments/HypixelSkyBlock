package net.swofty.types.generic.item.items.weapon.vanilla;

import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.ItemGroups;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiamondSword implements CustomSkyBlockItem, Enchantable, ExtraRarityDisplay, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 35)
                .build();
    }

    @Override
    public boolean showEnchantLores() {
        return true;
    }

    @Override
    public List<ItemGroups> getItemGroups() {
        return List.of(ItemGroups.SWORD);
    }

    @Override
    public String getExtraRarityDisplay() {
        return " SWORD";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('D', new MaterialQuantifiable(ItemType.DIRT, 3));
        ingredientMap.put('P', new MaterialQuantifiable(ItemType.IRON_PICKAXE, 1));
        List<String> pattern = List.of(
                "DD",
                "PD");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.DIAMOND_SWORD), ingredientMap, pattern);
    }
}
