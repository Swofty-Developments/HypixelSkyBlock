package net.swofty.commons.skyblock.event.actions.custom.island;

import net.minestom.server.event.Event;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.custom.IslandSavedIntoDatabaseEvent;

@EventParameters(description = "Handles Jerry on the players Island",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionIslandSaveJerry extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandSavedIntoDatabaseEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        IslandSavedIntoDatabaseEvent event = (IslandSavedIntoDatabaseEvent) tempEvent;

        if (event.getIsland().getJerryPosition() == null)
            return;

        event.getIsland().getDatabase().insertOrUpdate("jerry_position_x", event.getIsland().getJerryPosition().x());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_y", event.getIsland().getJerryPosition().y());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_z", event.getIsland().getJerryPosition().z());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_yaw", (double) event.getIsland().getJerryPosition().yaw());
        event.getIsland().getDatabase().insertOrUpdate("jerry_position_pitch", (double) event.getIsland().getJerryPosition().pitch());
    }
}
