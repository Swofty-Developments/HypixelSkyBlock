package net.swofty.item.impl;

import com.mongodb.lang.Nullable;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.ItemStatistics;

import java.util.ArrayList;

public interface CustomSkyBlockItem {
    ItemStatistics getStatistics();

    default ArrayList<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return null;
    }
}
