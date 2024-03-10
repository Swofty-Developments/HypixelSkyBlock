package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class EnchantedEmerald implements Enchanted, Sellable, MultiCraftable {

    @Override
    public double getSellValue() {
        return 960;
    }

    @Override
    public List<SkyBlockRecipe<?>> getRecipes() {
        List<SkyBlockRecipe<?>> recipes = new ArrayList<>();

        recipes.add(getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.MINING, ItemType.EMERALD));
        recipes.add(new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.ENCHANTED_EMERALD), 9)
                .add(ItemType.EMERALD_BLOCK, 32)
                .add(ItemType.EMERALD_BLOCK, 32)
                .add(ItemType.EMERALD_BLOCK, 32)
                .add(ItemType.EMERALD_BLOCK, 32)
                .add(ItemType.EMERALD_BLOCK, 32));

        return recipes;
    }
}