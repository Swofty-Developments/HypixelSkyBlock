package net.swofty.commons.skyblock.item.items.enchanted;


import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.impl.Enchanted;
import net.swofty.commons.skyblock.item.impl.Sellable;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;

public class EnchantedBone implements Enchanted, Sellable {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.BONE;
    }

    @Override
    public double getSellValue() {
        return 500;
    }

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.COMBAT;
    }
}