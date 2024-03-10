package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedCookie implements Enchanted, Sellable, Craftable {

    @Override
    public double getSellValue() {
        return 61500;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_COOKIE), 1)
                .add(ItemType.ENCHANTED_COCOA_BEANS, 32)
                .add(ItemType.ENCHANTED_COCOA_BEANS, 32)
                .add(ItemType.ENCHANTED_COCOA_BEANS, 32)
                .add(ItemType.ENCHANTED_COCOA_BEANS, 32)
                .add(ItemType.WHEAT, 32);
    }
}