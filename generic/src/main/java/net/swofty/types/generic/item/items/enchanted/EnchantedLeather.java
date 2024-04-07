package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedLeather implements Enchanted, Sellable, DefaultCraftable {
    @Override
    public double getSellValue() {
        return 1700;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_LEATHER), 1)
                .add(ItemType.LEATHER, 64)
                .add(ItemType.LEATHER, 64)
                .add(ItemType.LEATHER, 64)
                .add(ItemType.LEATHER, 64)
                .add(ItemType.LEATHER, 64)
                .add(ItemType.LEATHER, 64)
                .add(ItemType.LEATHER, 64)
                .add(ItemType.LEATHER, 64)
                .add(ItemType.LEATHER, 64);
    }
}