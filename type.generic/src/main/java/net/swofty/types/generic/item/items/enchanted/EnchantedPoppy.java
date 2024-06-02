package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedPoppy implements Enchanted, Sellable, DefaultCraftable {
    @Override
    public double getSellValue() {
        return 576;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FORAGING,
                new SkyBlockItem(ItemType.ENCHANTED_POPPY), 1)
                .add(ItemType.POPPY, 64)
                .add(ItemType.POPPY, 64)
                .add(ItemType.POPPY, 64)
                .add(ItemType.POPPY, 64)
                .add(ItemType.POPPY, 64)
                .add(ItemType.POPPY, 64)
                .add(ItemType.POPPY, 64)
                .add(ItemType.POPPY, 64)
                .add(ItemType.POPPY, 64);
    }
}