package net.swofty.types.generic.item.items.combat.slayer.wolf.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class GoldenTooth implements CustomSkyBlockItem, Sellable, DefaultCraftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.SVEN_PACKMASTER,
                new SkyBlockItem(ItemType.GOLDEN_TOOTH), 1)
                .add(ItemType.WOLF_TOOTH, 32)
                .add(ItemType.WOLF_TOOTH, 32)
                .add(ItemType.WOLF_TOOTH, 32)
                .add(ItemType.WOLF_TOOTH, 32)
                .add(ItemType.ENCHANTED_GOLD_INGOT, 32);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 128;
    }
}
