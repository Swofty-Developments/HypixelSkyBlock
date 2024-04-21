package net.swofty.types.generic.item.items.end.fragments;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class SuperiorDragonFragment implements CustomSkyBlockItem, RightClickRecipe, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.SUPERIOR_DRAGON_FRAGMENT;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "6f89b150be9c4c5249f355f68ea0c4391300a9be1f260d750fc35a1817ad796e";
    }
}
