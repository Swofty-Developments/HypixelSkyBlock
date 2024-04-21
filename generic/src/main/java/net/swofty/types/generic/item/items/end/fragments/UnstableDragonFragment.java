package net.swofty.types.generic.item.items.end.fragments;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class UnstableDragonFragment implements CustomSkyBlockItem, RightClickRecipe, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.UNSTABLE_DRAGON_FRAGMENT;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "98228c234c3903c512a5a0aa45260e7b567e0e20eefc7d561ccec97b295871af";
    }
}