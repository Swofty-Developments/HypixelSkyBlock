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
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FORAGING,
                new SkyBlockItem(ItemType.ENCHANTED_CHARCOAL), 1)
                .add(ItemType.COAL, 32)
                .add(ItemType.COAL, 32)
                .add(ItemType.COAL, 32)
                .add(ItemType.COAL, 32)
                .add(ItemType.OAK_WOOD, 32);
    }
}