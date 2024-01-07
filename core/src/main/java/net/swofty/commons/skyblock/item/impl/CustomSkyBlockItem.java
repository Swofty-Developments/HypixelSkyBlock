package net.swofty.commons.skyblock.item.impl;

import com.mongodb.lang.Nullable;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;

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
