package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantedCharcoal implements Enchanted, Sellable, DefaultCraftable {

    @Override
    public double getSellValue() {
        return 320;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FORAGING,
                new SkyBlockItem(ItemType.ENCHANTED_CHARCOAL), 1)
                .add(ItemType.COAL, 32)
                .add(ItemType.COAL, 32)
                .add(ItemType.COAL, 32)
                .add(ItemType.COAL, 32)
                .add(ItemType.OAK_WOOD, 32);
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Increases the speed of",
                "§7your minion by §a20%§7 for 36",
                "§7hours§7!"));
    }
}