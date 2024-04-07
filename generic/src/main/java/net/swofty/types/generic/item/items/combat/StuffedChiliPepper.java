package net.swofty.types.generic.item.items.combat;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class StuffedChiliPepper implements Enchanted, Sellable, SkullHead, DefaultCraftable {

    @Override
    public double getSellValue() {
        return 200000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f599f959f0ebcb1eb0f2e5852d122ca55bf5258fd4613ab50d72ad8cf5e12603";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7It\u0027s red hot, but green and",
                "§7stuffed!"));
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.COMBAT, ItemType.CHILI_PEPPER);
    }
}
