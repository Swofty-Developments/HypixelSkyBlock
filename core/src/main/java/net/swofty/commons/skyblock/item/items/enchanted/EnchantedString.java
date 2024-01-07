package net.swofty.commons.skyblock.item.items.enchanted;


import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.impl.Enchanted;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;

public class EnchantedString implements Enchanted {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.STRING;
    }

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.COMBAT;
    }
}