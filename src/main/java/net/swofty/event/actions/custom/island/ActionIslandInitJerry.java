package net.swofty.event.actions.custom.island;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.IslandFirstCreatedEvent;
import org.tinylog.Logger;

@EventParameters(description = "Handles creating Jerry on the players Island",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionIslandInitJerry extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandFirstCreatedEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        IslandFirstCreatedEvent event = (IslandFirstCreatedEvent) tempEvent;

        event.getIsland().setJerryPosition(
                new Pos(2.5, 100, 24.5, 145, 0)
        );
    }
}
