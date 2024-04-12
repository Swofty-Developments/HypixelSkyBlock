package net.swofty.types.generic.item.items.combat.slayer.enderman.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class NullOvoid implements CustomSkyBlockItem, DefaultCraftable, Enchanted {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.SLAYER,
                new SkyBlockItem(ItemType.NULL_OVOID), 1)
                .add(ItemType.NULL_SPHERE, 32)
                .add(ItemType.NULL_SPHERE, 32)
                .add(ItemType.NULL_SPHERE, 32)
                .add(ItemType.NULL_SPHERE, 32)
                .add(ItemType.ENCHANTED_OBSIDIAN, 32);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
}
