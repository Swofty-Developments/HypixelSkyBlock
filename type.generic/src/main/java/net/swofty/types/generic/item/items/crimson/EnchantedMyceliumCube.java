package net.swofty.types.generic.item.items.crimson;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class EnchantedMyceliumCube implements Enchanted, Sellable, DefaultCraftable, SkullHead {
    @Override
    public double getSellValue() {
        return 128000;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.MINING, ItemType.ENCHANTED_MYCELIUM);
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "9a351d5316e5b508fbbf595e0773aec9639a0891807fef09bc59a56d46490cb0";
    }
}