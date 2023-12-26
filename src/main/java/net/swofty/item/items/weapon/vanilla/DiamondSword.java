package net.swofty.item.items.weapon.vanilla;

import net.swofty.item.ItemType;
import net.swofty.item.MaterialQuantifiable;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.*;
import net.swofty.item.impl.recipes.ShapedRecipe;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.ItemGroups;

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
        List<String> pattern = List.of("DD", "PD");

        return new ShapedRecipe(new SkyBlockItem(ItemType.DIAMOND_SWORD), ingredientMap, pattern, (player -> {
            if (player.getLevel() > 10) {
                return new SkyBlockRecipe.CraftingResult(true, null);
            } else {
                return new SkyBlockRecipe.CraftingResult(false, new String[] {"§cLevel Issue", "§7You must be at least §eLevel 10 §7to craft this item!"});
            }
        }));
    }
}
