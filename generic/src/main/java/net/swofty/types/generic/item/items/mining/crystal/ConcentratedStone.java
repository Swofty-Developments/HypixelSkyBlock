package net.swofty.types.generic.item.items.mining.crystal;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class ConcentratedStone implements Enchanted, Sellable, DefaultCraftable, SkullHead {
    @Override
    public double getSellValue() {
        return 200000;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.CONCENTRATED_STONE), 1)
                .add(ItemType.ENCHANTED_HARD_STONE, 64)
                .add(ItemType.ENCHANTED_HARD_STONE, 64)
                .add(ItemType.ENCHANTED_HARD_STONE, 64)
                .add(ItemType.ENCHANTED_HARD_STONE, 64)
                .add(ItemType.ENCHANTED_HARD_STONE, 64)
                .add(ItemType.ENCHANTED_HARD_STONE, 64)
                .add(ItemType.ENCHANTED_HARD_STONE, 64)
                .add(ItemType.ENCHANTED_HARD_STONE, 64)
                .add(ItemType.ENCHANTED_HARD_STONE, 64);
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7The purest form of stone. How",
                "§7can something so simple, be so",
                "§7heavy?"));
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8c8637bc0628f5710f85b8d0679cc2c19afe0e1089b248b6fdb67f687ae81dc2";
    }
}