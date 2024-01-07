package net.swofty.commons.skyblock.event.actions.custom.island;

import net.minestom.server.event.Event;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.commons.skyblock.structure.structures.IslandPortal;

@EventParameters(description = "Handles creating portals on the players Island",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionIslandCreatePortals extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandFetchedFromDatabaseEvent.class;
    }

    @Override
    public void run(Event event) {
        IslandFetchedFromDatabaseEvent islandFetchedFromDatabaseEvent = (IslandFetchedFromDatabaseEvent) event;

        IslandPortal portal = new IslandPortal(0, -1, 100, 35);
        portal.setType(IslandPortal.PortalType.HUB);

        portal.build(islandFetchedFromDatabaseEvent.getIsland().getIslandInstance());
    }
}
