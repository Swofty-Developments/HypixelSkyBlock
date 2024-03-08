package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

public class EnchantedBlazeRod implements Enchanted, Sellable {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.ENCHANTED_BLAZE_POWDER;
    }

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.COMBAT;
    }

    @Override
    public double getSellValue() {
        return 230400;
    }
}