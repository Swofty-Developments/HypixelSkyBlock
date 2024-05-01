package net.swofty.type.island.events.custom;

import net.minestom.server.event.Event;
import net.swofty.types.generic.structure.structures.IslandPortal;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.IslandFetchedFromDatabaseEvent;

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
