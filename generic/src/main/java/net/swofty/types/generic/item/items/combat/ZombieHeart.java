package net.swofty.types.generic.item.items.combat;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class ZombieHeart implements Enchanted, Sellable, SkullHead, Craftable {

    @Override
    public double getSellValue() {
        return 123000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "71d7c816fc8c636d7f50a93a0ba7aaeff06c96a561645e9eb1bef391655c531";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                new SkyBlockItem(ItemType.ZOMBIE_HEART), 1)
                .add(ItemType.ENCHANTED_ROTTEN_FLESH, 32)
                .add(ItemType.ENCHANTED_ROTTEN_FLESH, 32)
                .add(ItemType.ENCHANTED_ROTTEN_FLESH, 32)
                .add(ItemType.ENCHANTED_ROTTEN_FLESH, 32)
                .add(ItemType.ENCHANTED_ROTTEN_FLESH, 32)
                .add(ItemType.ENCHANTED_ROTTEN_FLESH, 32)
                .add(ItemType.ENCHANTED_ROTTEN_FLESH, 32)
                .add(ItemType.ENCHANTED_ROTTEN_FLESH, 32);
    }
}
