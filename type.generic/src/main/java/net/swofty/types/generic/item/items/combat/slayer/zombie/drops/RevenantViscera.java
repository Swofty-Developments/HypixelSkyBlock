package net.swofty.types.generic.item.items.combat.slayer.zombie.drops;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.commons.statistics.ItemStatistics;

public class RevenantViscera implements CustomSkyBlockItem, Sellable, Enchanted, DefaultCraftable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 128;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.SLAYER,
                new SkyBlockItem(ItemTypeLinker.REVENANT_VISCERA), 1)
                .add(ItemTypeLinker.REVENANT_FLESH, 32)
                .add(ItemTypeLinker.REVENANT_FLESH, 32)
                .add(ItemTypeLinker.REVENANT_FLESH, 32)
                .add(ItemTypeLinker.REVENANT_FLESH, 32)
                .add(ItemTypeLinker.ENCHANTED_STRING, 32);
    }
}
