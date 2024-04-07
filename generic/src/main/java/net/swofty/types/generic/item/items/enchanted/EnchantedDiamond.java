package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MultiDefaultCraftable;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class EnchantedDiamond implements Enchanted, Sellable, MultiDefaultCraftable {

    @Override
    public double getSellValue() {
        return 1280;
    }

    @Override
    public List<SkyBlockRecipe<?>> getRecipes() {
        List<SkyBlockRecipe<?>> recipes = new ArrayList<>();

        recipes.add(getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.MINING, ItemType.DIAMOND));
        recipes.add(new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.ENCHANTED_DIAMOND), 9)
                .add(ItemType.DIAMOND_BLOCK, 32)
                .add(ItemType.DIAMOND_BLOCK, 32)
                .add(ItemType.DIAMOND_BLOCK, 32)
                .add(ItemType.DIAMOND_BLOCK, 32)
                .add(ItemType.DIAMOND_BLOCK, 32));

        return recipes;
    }
}