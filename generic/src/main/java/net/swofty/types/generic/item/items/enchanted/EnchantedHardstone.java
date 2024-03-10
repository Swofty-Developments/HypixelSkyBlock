package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantedHardstone implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 576;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.ENCHANTED_HARD_STONE), 1)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64)
                .add(ItemType.HARD_STONE, 64);
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7A refined form of heavy rock",
                "§7from the darkest depths."));
    }
}