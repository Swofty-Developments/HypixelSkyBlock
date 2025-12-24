package net.swofty.type.island.events.custom;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.event.custom.IslandFirstCreatedEvent;
import net.swofty.type.skyblockgeneric.minion.IslandMinionData;
import net.swofty.type.skyblockgeneric.minion.MinionRegistry;

public class ActionPlayerSpawnFirstMinion implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(IslandFirstCreatedEvent event) {
        MathUtility.delay(() -> {
            IslandMinionData.IslandMinion minion = event.getIsland().getMinionData().initializeMinion(
                    new Pos(-4, 100, 28), // Default Cobble Minion position
                    MinionRegistry.COBBLESTONE,
                    new ItemAttributeMinionData.MinionData(1, 0),
                    false
            );

            event.getIsland().getMinionData().spawn(minion);
        }, 40);  // delay to let island load properly
    }
}
