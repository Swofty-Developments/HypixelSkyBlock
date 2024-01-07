package net.swofty.commons.skyblock.item.items.enchanted;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.impl.Enchanted;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;

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