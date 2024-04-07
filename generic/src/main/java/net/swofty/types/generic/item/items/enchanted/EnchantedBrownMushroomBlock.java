package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class EnchantedBrownMushroomBlock implements Enchanted, Sellable, MultiDefaultCraftable {

    @Override
    public double getSellValue() {
        return 51200;
    }

    @Override
    public List<SkyBlockRecipe<?>> getRecipes() {
        List<SkyBlockRecipe<?>> recipes = new ArrayList<>();

        recipes.add(new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_BROWN_MUSHROOM_BLOCK), 1)
                .add(ItemType.ENCHANTED_BROWN_MUSHROOM, 8)
                .add(ItemType.ENCHANTED_BROWN_MUSHROOM, 8)
                .add(ItemType.ENCHANTED_BROWN_MUSHROOM, 8)
                .add(ItemType.ENCHANTED_BROWN_MUSHROOM, 8));
        recipes.add(new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_BROWN_MUSHROOM_BLOCK), 1)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64)
                .add(ItemType.BROWN_MUSHROOM_BLOCK, 64));

        return recipes;
    }
}