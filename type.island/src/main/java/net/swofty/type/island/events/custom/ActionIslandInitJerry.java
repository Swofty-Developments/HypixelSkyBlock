package net.swofty.type.island.events.custom;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.IslandFirstCreatedEvent;
import net.swofty.type.skyblockgeneric.utility.JerryInformation;

public class ActionIslandInitJerry implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(IslandFirstCreatedEvent event) {
        event.getIsland().setJerryInformation(
                new JerryInformation(null, new Pos(2.5, 100, 24.5, 145, 0), null)
        );
    }
}
