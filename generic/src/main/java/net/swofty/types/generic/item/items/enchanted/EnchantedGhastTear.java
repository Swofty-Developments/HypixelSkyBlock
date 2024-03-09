package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

public class EnchantedGhastTear implements Enchanted, Sellable {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.GHAST_TEAR;
    }

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.COMBAT;
    }

    @Override
    public double getSellValue() {
        return 80;
    }
}