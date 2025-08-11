package net.swofty.type.island.events.custom;

import net.swofty.type.skyblockgeneric.event.EventNodes;
import net.swofty.type.skyblockgeneric.event.SkyBlockEvent;
import net.swofty.type.skyblockgeneric.event.SkyBlockEventClass;
import net.swofty.type.skyblockgeneric.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.type.skyblockgeneric.structure.structures.IslandPortal;

public class ActionIslandCreatePortals implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(IslandFetchedFromDatabaseEvent event) {
        IslandPortal portal = new IslandPortal(0, -1, 100, 35);
        portal.setType(IslandPortal.PortalType.HUB);

        portal.build(event.getIsland().getIslandInstance());
    }
}
