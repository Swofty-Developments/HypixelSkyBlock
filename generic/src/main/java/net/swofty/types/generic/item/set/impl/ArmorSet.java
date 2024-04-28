package net.swofty.types.generic.item.set.impl;

import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

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
