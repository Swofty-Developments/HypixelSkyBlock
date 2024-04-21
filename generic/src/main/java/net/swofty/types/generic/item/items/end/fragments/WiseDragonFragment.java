package net.swofty.types.generic.item.items.end.fragments;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class WiseDragonFragment implements CustomSkyBlockItem, RightClickRecipe, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.WISE_DRAGON_FRAGMENT;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "1d7620b2e4934963bb12508310d05494c067dc33e008cecf2cd7b4549654fab3";
    }
}