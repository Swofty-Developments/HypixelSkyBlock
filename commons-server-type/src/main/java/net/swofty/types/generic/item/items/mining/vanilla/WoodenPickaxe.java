package net.swofty.types.generic.item.items.mining.vanilla;

import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.ItemGroups;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WoodenPickaxe implements CustomSkyBlockItem, MiningTool, ExtraRarityDisplay, Enchantable, Reforgable, Craftable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.MINING_SPEED, 70)
                .with(ItemStatistic.DAMAGE, 15)
                .build();
    }

    @Override
    public int getBreakingPower() {
        return 1;
    }

    @Override
    public String getExtraRarityDisplay() {
        return " PICKAXE";
    }

    @Override
    public boolean showEnchantLores() {
        return true;
    }

    @Override
    public List<ItemGroups> getItemGroups() {
        return List.of(ItemGroups.PICKAXE, ItemGroups.TOOLS);
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.PICKAXES;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('W', new MaterialQuantifiable(ItemType.OAK_PLANKS, 1));
        ingredientMap.put('S', new MaterialQuantifiable(ItemType.STICK, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "WWW",
                " S ",
                " S ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.WOODEN_PICKAXE), ingredientMap, pattern);
    }
}
