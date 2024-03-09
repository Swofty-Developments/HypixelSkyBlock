package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

public class EnchantedBrownMushroom implements Enchanted, Sellable {
    @Override
    public ItemType getCraftingMaterial() {
        return ItemType.BROWN_MUSHROOM;
    }
    //also 160 Brown Mushroom Blocks into 9 Enchanted Brown Mushrooms

    @Override
    public SkyBlockRecipe.RecipeType getRecipeType() {
        return SkyBlockRecipe.RecipeType.FARMING;
    }

    @Override
    public double getSellValue() {
        return 1600;
    }
}
