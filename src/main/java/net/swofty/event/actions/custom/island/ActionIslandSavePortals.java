package net.swofty.event.actions.custom.island;

import net.minestom.server.event.Event;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.IslandSavedIntoDatabaseEvent;

@EventParameters(description = "Handles saving portals on the players Island",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionIslandSavePortals extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandSavedIntoDatabaseEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        IslandSavedIntoDatabaseEvent event = (IslandSavedIntoDatabaseEvent) tempEvent;


    }
}
