package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

import java.util.List;
import net.swofty.types.generic.item.impl.Craftable;

public class EnchantedSugarCane implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 102400;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.FARMING, ItemType.ENCHANTED_SUGAR);
    }
}
