package net.swofty.commons.skyblock.item.set.impl;

import net.swofty.commons.skyblock.SkyBlock;
import net.swofty.commons.skyblock.item.set.ArmorSetRegistry;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public interface ArmorSet {
    String getName();

    String getDescription();

    default boolean isWearingSet(SkyBlockPlayer player) {
        return player.getArmorSet() != null && player.getArmorSet().equals(ArmorSetRegistry.getArmorSet(this.getClass()));
    }

    default List<SkyBlockPlayer> getWearingSet() {
        ArrayList<SkyBlockPlayer> toReturn = new ArrayList<>();
        for (SkyBlockPlayer player : SkyBlock.getLoadedPlayers()) {
            if (player.getArmorSet() != null && player.getArmorSet().equals(ArmorSetRegistry.getArmorSet(this.getClass()))) {
                toReturn.add(player);
            }
        }
        return toReturn;
    }
}
