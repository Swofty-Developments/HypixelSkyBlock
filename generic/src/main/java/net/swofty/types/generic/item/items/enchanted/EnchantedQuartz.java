package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MultiCraftable;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class EnchantedQuartz implements Enchanted, Sellable, MultiCraftable {
    @Override
    public double getSellValue() {
        return 640;
    }

    @Override
    public List<SkyBlockRecipe<?>> getRecipes() {
        List<SkyBlockRecipe<?>> recipes = new ArrayList<>();

        recipes.add(getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.MINING, ItemType.QUARTZ));
        recipes.add(new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.ENCHANTED_QUARTZ), 9)
                .add(ItemType.QUARTZ_BLOCK, 32)
                .add(ItemType.QUARTZ_BLOCK, 32)
                .add(ItemType.QUARTZ_BLOCK, 32)
                .add(ItemType.QUARTZ_BLOCK, 32)
                .add(ItemType.QUARTZ_BLOCK, 32));

        return recipes;
    }
}