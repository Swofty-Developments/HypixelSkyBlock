package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantedEgg implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 432;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_EGG), 1)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16)
                .add(ItemType.EGG, 16);
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7This item can be used as a",
                "§7minion upgrade for chicken",
                "§7minions. Guarantees that each",
                "§7chicken will drop an egg after",
                "§7they spawn.",
                "§7",
                "§7Can also be used to craft",
                "§7low-rarity pets."));
    }
}