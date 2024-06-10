package net.swofty.types.generic.item.items.combat.slayer.wolf.craftable;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.commons.statistics.ItemStatistics;

public class GoldenTooth implements CustomSkyBlockItem, Sellable, DefaultCraftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.SVEN_PACKMASTER,
                new SkyBlockItem(ItemTypeLinker.GOLDEN_TOOTH), 1)
                .add(ItemTypeLinker.WOLF_TOOTH, 32)
                .add(ItemTypeLinker.WOLF_TOOTH, 32)
                .add(ItemTypeLinker.WOLF_TOOTH, 32)
                .add(ItemTypeLinker.WOLF_TOOTH, 32)
                .add(ItemTypeLinker.ENCHANTED_GOLD_INGOT, 32);
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
