package net.swofty.item.impl;

import java.util.ArrayList;

public interface CustomSkyBlockItem {
    ItemStatistics getStatistics();

    default ArrayList<String> getLore() {
        return null;
    }
}
