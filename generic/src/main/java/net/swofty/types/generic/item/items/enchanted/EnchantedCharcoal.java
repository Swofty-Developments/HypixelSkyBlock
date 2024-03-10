package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedCharcoal implements Enchanted, Sellable, Craftable {

    @Override
    public double getSellValue() {
        return 320;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        // Adjusting the recipe as per the comment that 128 coal and 32 oak wood should be used
        // Assuming a custom method for recipes that don't follow the standard pattern
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.ENCHANTED_CHARCOAL))
                .add(ItemType.COAL, 64)
                .add(ItemType.COAL, 64)
                .add(ItemType.OAK_WOOD, 32);
    }
}