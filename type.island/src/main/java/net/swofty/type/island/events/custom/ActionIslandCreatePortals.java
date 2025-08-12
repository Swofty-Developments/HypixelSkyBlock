package net.swofty.type.island.events.custom;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.SkyBlockEvent;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.type.generic.structure.structures.IslandPortal;

public class ActionIslandCreatePortals implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(IslandFetchedFromDatabaseEvent event) {
        IslandPortal portal = new IslandPortal(0, -1, 100, 35);
        portal.setType(IslandPortal.PortalType.HUB);

        portal.build(event.getIsland().getIslandInstance());
    }
}
