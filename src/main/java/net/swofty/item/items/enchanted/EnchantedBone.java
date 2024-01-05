package net.swofty.item.items.enchanted;


import net.swofty.item.ItemType;
import net.swofty.item.impl.Enchanted;
import net.swofty.item.impl.Sellable;
import net.swofty.item.impl.SkyBlockRecipe;

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