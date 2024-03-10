package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;

import java.util.List;

public class EnchantedBrownMushroom implements Enchanted, Sellable, MultiCraftable {

    @Override
    public double getSellValue() {
        return 1600;
    }

    @Override
    public List<SkyBlockRecipe<?>> getRecipes() {
        return List.of(
                getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.FARMING, ItemType.BROWN_MUSHROOM),
                new ShapelessRecipe(SkyBlockRecipe.RecipeType.FORAGING,
                        new SkyBlockItem(ItemType.ENCHANTED_BROWN_MUSHROOM), 9)
                        .add(ItemType.BROWN_MUSHROOM, 64)
                        .add(ItemType.BROWN_MUSHROOM, 64)
                        .add(ItemType.BROWN_MUSHROOM, 32)
        );
    }
}
