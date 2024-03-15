package net.swofty.types.generic.item.items.combat;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class AbsoluteEnderPearl implements Enchanted, Sellable, Craftable, SkullHead {
    @Override
    public double getSellValue() {
        return 11200;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT,
                new SkyBlockItem(ItemType.ABSOLUTE_ENDER_PEARL), 1)
                .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                .add(ItemType.ENCHANTED_ENDER_PEARL, 16)
                .add(ItemType.ENCHANTED_ENDER_PEARL, 16);
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "67d9fe065024fff4b34c78f92cd3150d6bdfaab7c375a0ee785076e1a4a254e9";
    }
}