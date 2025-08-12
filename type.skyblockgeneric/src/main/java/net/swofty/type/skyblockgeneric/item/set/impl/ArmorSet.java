package net.swofty.type.skyblockgeneric.item.set.impl;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.item.set.ArmorSetRegistry;
import SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public interface ArmorSet {
    String getName();

    ArrayList<String> getDescription();

    default ItemStatistics getStatistics() {
        return ItemStatistics.empty();
    }

    default boolean isWearingSet(SkyBlockPlayer player) {
        return player.getArmorSet() != null && player.getArmorSet().equals(ArmorSetRegistry.getArmorSet(this.getClass()));
    }

    default List<SkyBlockPlayer> getWearingSet() {
        ArrayList<SkyBlockPlayer> toReturn = new ArrayList<>();
        for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
            if (player.getArmorSet() != null && player.getArmorSet().equals(ArmorSetRegistry.getArmorSet(this.getClass()))) {
                toReturn.add(player);
            }
        }
        return toReturn;
    }
}
