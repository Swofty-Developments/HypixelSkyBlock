package net.swofty.types.generic.item.items.combat;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class WhippedMagmaCream implements Enchanted, Sellable, SkullHead, Craftable {

    @Override
    public double getSellValue() {
        return 204800;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "7da530ad09e69e4678c69cf5b8e0f0751e4630355fc7f0c226c955baace30fb2";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.COMBAT, ItemType.ENCHANTED_MAGMA_CREAM);
    }
}
