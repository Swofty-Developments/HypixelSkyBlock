package net.swofty.types.generic.item.items.combat.slayer.zombie.drops;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class BeheadedHorror implements CustomSkyBlockItem, SkullHead, Sellable, TrackedUniqueItem, RightClickRecipe {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 15000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "dbad99ed3c820b7978190ad08a934a68dfa90d9986825da1c97f6f21f49ad626";
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.REAPER_MASK;
    }
}