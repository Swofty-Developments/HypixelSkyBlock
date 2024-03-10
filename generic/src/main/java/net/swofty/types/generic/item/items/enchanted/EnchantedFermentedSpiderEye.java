package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

public class EnchantedFermentedSpiderEye implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 31000;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                new SkyBlockItem(ItemType.ENCHANTED_FERMENTED_SPIDER_EYE), 1)
                .add(ItemType.BROWN_MUSHROOM, 64)
                .add(ItemType.SUGAR, 64)
                .add(ItemType.ENCHANTED_SPIDER_EYE, 64);
    }
}