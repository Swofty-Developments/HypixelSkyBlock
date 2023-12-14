package net.swofty.item.impl;

import net.swofty.user.statistics.ItemStatistics;

import java.util.ArrayList;

public interface CustomSkyBlockItem {
    ItemStatistics getStatistics();

    default ArrayList<String> getLore() {
        return null;
    }
}
