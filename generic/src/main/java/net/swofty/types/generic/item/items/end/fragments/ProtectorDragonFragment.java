package net.swofty.types.generic.item.items.end.fragments;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.RightClickRecipe;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ProtectorDragonFragment implements CustomSkyBlockItem, RightClickRecipe, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ItemType getRecipeItem() {
        return ItemType.PROTECTOR_DRAGON_FRAGMENT;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "48de339af63a229c9238d027e47f53eeb56141a419f51b35c31ea1494b435dd3";
    }
}
