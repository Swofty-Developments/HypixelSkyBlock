package net.swofty.commons.skyblock.event.actions.custom.island;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.custom.IslandFirstCreatedEvent;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;

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
