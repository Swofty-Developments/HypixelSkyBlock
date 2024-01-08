package net.swofty.type.island.events.custom;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.IslandFirstCreatedEvent;

@EventParameters(description = "Handles creating Jerry on the players Island",
        node = EventNodes.CUSTOM,
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
