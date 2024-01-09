package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

public class EnchantedRottenFlesh implements Enchanted {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.ROTTEN_FLESH;
    }

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.COMBAT;
    }
}