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

public class SuperEnchantedEgg implements Enchanted, Craftable {

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.SUPER_ENCHANTED_EGG), 1)
                .add(ItemType.ENCHANTED_EGG, 16)
                .add(ItemType.ENCHANTED_EGG, 16)
                .add(ItemType.ENCHANTED_EGG, 16)
                .add(ItemType.ENCHANTED_EGG, 16)
                .add(ItemType.ENCHANTED_EGG, 16)
                .add(ItemType.ENCHANTED_EGG, 16)
                .add(ItemType.ENCHANTED_EGG, 16)
                .add(ItemType.ENCHANTED_EGG, 16)
                .add(ItemType.ENCHANTED_EGG, 16);
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7Used to craft epic and",
                "§7legendary pets.",
                "",
                "§eRight-click to view recipes!"));
    }
}