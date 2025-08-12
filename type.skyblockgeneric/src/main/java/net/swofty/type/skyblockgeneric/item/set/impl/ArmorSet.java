package net.swofty.type.skyblockgeneric.item.set.impl;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.type.generic.SkyBlockGenericLoader;
import net.swofty.type.generic.item.set.ArmorSetRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public interface ArmorSet {
    String getName();

    ArrayList<String> getDescription();

    default ItemStatistics getStatistics() {
        return ItemStatistics.empty();
    }

    default boolean isWearingSet(HypixelPlayer player) {
        return player.getArmorSet() != null && player.getArmorSet().equals(ArmorSetRegistry.getArmorSet(this.getClass()));
    }

    default List<HypixelPlayer> getWearingSet() {
        ArrayList<HypixelPlayer> toReturn = new ArrayList<>();
        for (HypixelPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
            if (player.getArmorSet() != null && player.getArmorSet().equals(ArmorSetRegistry.getArmorSet(this.getClass()))) {
                toReturn.add(player);
            }
        }
        return toReturn;
    }
}
