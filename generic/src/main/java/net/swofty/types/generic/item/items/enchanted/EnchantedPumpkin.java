package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

public class EnchantedPumpkin implements Enchanted {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.PUMPKIN;
    }

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.FARMING;
    }
}