package net.swofty.types.generic.item.items.backpacks;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Backpack;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;
public class JumboBackpack implements Backpack, Craftable {
    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "62f3b3a05481cde77240005c0ddcee1c069e5504a62ce0977879f55a39396146";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        ShapelessRecipe recipe = new ShapelessRecipe(SkyBlockRecipe.RecipeType.FORAGING, new SkyBlockItem(ItemType.JUMBO_BACKPACK));
        recipe.add(ItemType.GREATER_BACKPACK, 1);
        recipe.add(ItemType.JUMBO_BACKPACK_UPGRADE, 1);

        return recipe;
    }
}

