package net.swofty.type.island.events.custom;

import net.swofty.type.skyblockgeneric.event.EventNodes;
import net.swofty.type.skyblockgeneric.event.SkyBlockEvent;
import net.swofty.type.skyblockgeneric.event.SkyBlockEventClass;
import net.swofty.type.skyblockgeneric.event.custom.IslandSavedIntoDatabaseEvent;
import net.swofty.type.skyblockgeneric.utility.JerryInformation;

public class ActionIslandSaveJerry implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(IslandSavedIntoDatabaseEvent event) {
        JerryInformation jerryInformation = event.getIsland().getJerryInformation();

        event.getIsland().getDatabase().insertOrUpdate("jerry_position_x", jerryInformation.getJerryPosition().x());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_y", jerryInformation.getJerryPosition().y());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_z", jerryInformation.getJerryPosition().z());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_yaw", (double) jerryInformation.getJerryPosition().yaw());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_pitch", (double) jerryInformation.getJerryPosition().pitch());
    }
}
