package net.swofty.types.generic.item.items.backpacks;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Backpack;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class JumboBackpack implements Backpack, DefaultCraftable {
    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "1f8405116c1daa7ce2f012591458d50246d0a467bcb95a5a2c033aefd6008b63";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        ShapelessRecipe recipe = new ShapelessRecipe(SkyBlockRecipe.RecipeType.SPECIAL, new SkyBlockItem(ItemType.JUMBO_BACKPACK));
        recipe.add(ItemType.GREATER_BACKPACK, 1);
        recipe.add(ItemType.JUMBO_BACKPACK_UPGRADE, 1);

        return recipe;
    }
}

