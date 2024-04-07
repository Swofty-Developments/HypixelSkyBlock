package net.swofty.types.generic.item.items.farming;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class TightlyTiedHayBale implements Enchanted, Sellable, DefaultCraftable, SkullHead {
    @Override
    public double getSellValue() {
        return 1119744;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.FARMING,
                new SkyBlockItem(ItemType.TIGHTLY_TIED_HAY_BALE), 1)
                .add(ItemType.ENCHANTED_HAY_BAL, 16)
                .add(ItemType.ENCHANTED_HAY_BAL, 16)
                .add(ItemType.ENCHANTED_HAY_BAL, 16)
                .add(ItemType.ENCHANTED_HAY_BAL, 16)
                .add(ItemType.ENCHANTED_HAY_BAL, 16)
                .add(ItemType.ENCHANTED_HAY_BAL, 16)
                .add(ItemType.ENCHANTED_HAY_BAL, 16)
                .add(ItemType.ENCHANTED_HAY_BAL, 16)
                .add(ItemType.ENCHANTED_HAY_BAL, 16);
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f7c33cd0c14ba830da149907f7a6aae835b6a35aea01e0ce073fb3c59cc46326";
    }
}