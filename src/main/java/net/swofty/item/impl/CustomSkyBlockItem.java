package net.swofty.item.impl;

import com.mongodb.lang.Nullable;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.ItemStatistics;

import java.util.List;

public interface CustomSkyBlockItem {
    ItemStatistics getStatistics();

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
