package net.swofty.types.generic.item.items.end.fragments;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class HolyDragonFragment implements CustomSkyBlockItem, RightClickRecipe, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.HOLY_DRAGON_FRAGMENT;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "4594ed467b6b3fc89de1796ec2b2374b92006e3e30166c3107487c6a7a335845";
    }
}
