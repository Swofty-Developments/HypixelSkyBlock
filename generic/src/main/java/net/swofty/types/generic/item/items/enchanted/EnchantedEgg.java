package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedEgg implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 432;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_EGG), 1)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16);
    }
}