package net.swofty.type.island.events.custom;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.IslandSavedIntoDatabaseEvent;
import net.swofty.types.generic.utility.JerryInformation;

public class ActionIslandSaveJerry extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandSavedIntoDatabaseEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        IslandSavedIntoDatabaseEvent event = (IslandSavedIntoDatabaseEvent) tempEvent;

        JerryInformation jerryInformation = event.getIsland().getJerryInformation();

        event.getIsland().getDatabase().insertOrUpdate("jerry_position_x", jerryInformation.getJerryPosition().x());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_y", jerryInformation.getJerryPosition().y());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_z", jerryInformation.getJerryPosition().z());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_yaw", (double) jerryInformation.getJerryPosition().yaw());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_pitch", (double) jerryInformation.getJerryPosition().pitch());
    }
}
