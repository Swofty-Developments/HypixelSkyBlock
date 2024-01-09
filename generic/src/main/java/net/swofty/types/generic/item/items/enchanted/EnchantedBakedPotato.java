package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

public class EnchantedBakedPotato implements Enchanted {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.BAKED_POTATO;
    }

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.FARMING;
    }
}