package net.swofty.types.generic.item.impl;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public interface Talisman extends CustomSkyBlockItem, SkullHead, ExtraRarityDisplay, TrackedUniqueItem, Accessory,
                                  ConstantStatistics {
    List<String> getTalismanDisplay();

    @Override
    default String getExtraRarityDisplay() {
        return " ACCESSORY";
    }

    @Override
    default List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return getTalismanDisplay();
    }

    @Override
    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
}
