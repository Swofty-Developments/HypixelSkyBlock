package net.swofty.types.generic.item.impl;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public interface CustomSkyBlockItem {
    ItemStatistics getStatistics(@Nullable SkyBlockItem instance);

    default boolean isPlaceable() {
        return false;
    }

    default List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return null;
    }

    default List<String> getAbsoluteLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return null;
    }

    default String getAbsoluteName(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "";
    }
}
