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

public class EnchantedBread implements Enchanted, Sellable, Craftable {

    @Override
    public double getSellValue() {
        return 60;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.ENCHANTED_BREAD), 1)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10)
                .add(ItemType.WHEAT, 10);
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7increases the speed of",
                "§7your minion by §a5%§7",
                "§7for 12 hours§7!"));
    }
}