package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

public class EnchantedCharcoal implements Enchanted, Sellable {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.CHARCOAL;
    }
    //wrong, 128 coal and 32 oak wood should be used

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.FORAGING;
    }

    @Override
    public double getSellValue() {
        return 320;
    }
}