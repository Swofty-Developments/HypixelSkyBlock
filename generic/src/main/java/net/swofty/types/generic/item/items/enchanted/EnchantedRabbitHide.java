package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedRabbitHide implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 2880;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_RABBIT_HIDE), 1)
                .add(ItemType.RABBIT_HIDE, 64)
                .add(ItemType.RABBIT_HIDE, 64)
                .add(ItemType.RABBIT_HIDE, 64)
                .add(ItemType.RABBIT_HIDE, 64)
                .add(ItemType.RABBIT_HIDE, 64)
                .add(ItemType.RABBIT_HIDE, 64)
                .add(ItemType.RABBIT_HIDE, 64)
                .add(ItemType.RABBIT_HIDE, 64)
                .add(ItemType.RABBIT_HIDE, 64);
    }
}