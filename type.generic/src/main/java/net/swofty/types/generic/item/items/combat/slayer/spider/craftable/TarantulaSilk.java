package net.swofty.types.generic.item.items.combat.slayer.spider.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class TarantulaSilk implements CustomSkyBlockItem, Sellable, DefaultCraftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER,
                new SkyBlockItem(ItemType.TARANTULA_SILK), 1)
                .add(ItemType.TARANTULA_WEB, 32)
                .add(ItemType.TARANTULA_WEB, 32)
                .add(ItemType.TARANTULA_WEB, 32)
                .add(ItemType.TARANTULA_WEB, 32)
                .add(ItemType.ENCHANTED_FLINT, 32);
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
