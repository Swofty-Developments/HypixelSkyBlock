package net.swofty.type.island.events.custom;

import net.swofty.type.generic.entity.hologram.ServerHolograms;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.IslandSavedIntoDatabaseEvent;
import net.swofty.type.skyblockgeneric.utility.JerryInformation;

public class ActionIslandSaveJerry implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(IslandSavedIntoDatabaseEvent event) {
        JerryInformation jerryInformation = event.getIsland().getJerryInformation();

        if (jerryInformation == null) return;

        if (jerryInformation.getJerry() != null) {
            jerryInformation.getJerry().remove();
        }
        if (jerryInformation.getHologram() != null) {
            ServerHolograms.removeExternalHologram(jerryInformation.getHologram());
        }

        if (jerryInformation.getJerryPosition() == null) return;

        event.getIsland().getDatabase().insertOrUpdate("jerry_position_x", jerryInformation.getJerryPosition().x());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_y", jerryInformation.getJerryPosition().y());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_z", jerryInformation.getJerryPosition().z());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_yaw", (double) jerryInformation.getJerryPosition().yaw());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_pitch", (double) jerryInformation.getJerryPosition().pitch());
    }
}
