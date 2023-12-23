package net.swofty.event.actions.custom.island;

import net.minestom.server.event.Event;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.IslandCreatedEvent;
import net.swofty.structure.structures.IslandPortal;

@EventParameters(description = "Handles creating portals on the players Island",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionIslandCreatePortals extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandCreatedEvent.class;
    }

    @Override
    public void run(Event event) {
        IslandCreatedEvent islandCreatedEvent = (IslandCreatedEvent) event;

        IslandPortal portal = new IslandPortal(0, -1, 100, 35);
        portal.setType(IslandPortal.PortalType.HUB);

        portal.build(islandCreatedEvent.getIsland().getIslandInstance());
    }
}
