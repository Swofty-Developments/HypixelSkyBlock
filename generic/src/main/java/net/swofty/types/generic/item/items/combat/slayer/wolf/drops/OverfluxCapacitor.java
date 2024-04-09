package net.swofty.types.generic.item.items.combat.slayer.wolf.drops;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.types.generic.item.impl.Unstackable;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class OverfluxCapacitor implements CustomSkyBlockItem, Enchanted, Unstackable, RightClickRecipe {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.EMPTY;
    }

    @Override
    public ItemType getRecipeItem() {
        return null;
    }
}
