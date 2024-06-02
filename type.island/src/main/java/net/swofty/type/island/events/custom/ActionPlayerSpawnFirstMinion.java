package net.swofty.type.island.events.custom;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.IslandFirstCreatedEvent;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.utility.MathUtility;

public class ActionPlayerSpawnFirstMinion implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
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
