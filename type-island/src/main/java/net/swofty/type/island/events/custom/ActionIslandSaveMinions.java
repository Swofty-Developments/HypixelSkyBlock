package net.swofty.type.island.events.custom;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.IslandSavedIntoDatabaseEvent;
import net.swofty.types.generic.minion.IslandMinionData;

public class ActionIslandSaveMinions implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM ,requireDataLoaded = false)
    public void run(IslandSavedIntoDatabaseEvent event) {
        IslandMinionData minionData = event.getIsland().getMinionData();
        minionData.getMinions().forEach(IslandMinionData.IslandMinion::removeMinion);

        event.getIsland().getDatabase().insertOrUpdate("minions", event.getIsland().getMinionData().serialize());
    }
}
