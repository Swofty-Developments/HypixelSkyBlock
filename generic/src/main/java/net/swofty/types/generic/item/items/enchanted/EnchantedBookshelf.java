package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class EnchantedBookshelf implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 2700;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_BOOKSHELF), 1)
                .add(ItemType.ENCHANTED_OAK_WOOD, 1)
                .add(ItemType.ENCHANTED_OAK_WOOD, 1)
                .add(ItemType.ENCHANTED_OAK_WOOD, 1)
                .add(ItemType.ENCHANTED_PAPER, 2)
                .add(ItemType.ENCHANTED_PAPER, 2)
                .add(ItemType.ENCHANTED_PAPER, 2)
                .add(ItemType.ENCHANTED_OAK_WOOD, 1)
                .add(ItemType.ENCHANTED_OAK_WOOD, 1)
                .add(ItemType.ENCHANTED_OAK_WOOD, 1);
    }
}