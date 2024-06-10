package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

public class EnchantedWool implements Enchanted, Sellable, DefaultCraftable {
    @Override
    public double getSellValue() {
        return 320;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.FARMING, ItemTypeLinker.WHITE_WOOL);
    }
}