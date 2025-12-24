package net.swofty.type.skyblockgeneric.item.set.impl;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.set.ArmorSetRegistry;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

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

    default int getWornPieceCount(SkyBlockPlayer player) {
        ArmorSetRegistry registry = ArmorSetRegistry.getArmorSet(this.getClass());
        if (registry == null) return 0;

        ItemType helmet = new SkyBlockItem(player.getHelmet()).getAttributeHandler().getPotentialType();
        ItemType chestplate = new SkyBlockItem(player.getChestplate()).getAttributeHandler().getPotentialType();
        ItemType leggings = new SkyBlockItem(player.getLeggings()).getAttributeHandler().getPotentialType();
        ItemType boots = new SkyBlockItem(player.getBoots()).getAttributeHandler().getPotentialType();

        return registry.getWornPieceCount(boots, leggings, chestplate, helmet);
    }

    default boolean hasAtLeastPieces(SkyBlockPlayer player, int minPieces) {
        return getWornPieceCount(player) >= minPieces;
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
