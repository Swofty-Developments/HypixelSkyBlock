package net.swofty.types.generic.item.items.crimson;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class EnchantedRedSandCube implements Enchanted, Sellable, DefaultCraftable, SkullHead {
    @Override
    public double getSellValue() {
        return 128000;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.MINING, ItemType.ENCHANTED_RED_SAND);
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f31f5c78b7e961591293756b9e15012db10457846c5ebc5db0f2b0e1873468ee";
    }
}