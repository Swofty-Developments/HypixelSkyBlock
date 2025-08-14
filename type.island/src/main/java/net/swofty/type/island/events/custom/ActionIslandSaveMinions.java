package net.swofty.type.island.events.custom;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.IslandSavedIntoDatabaseEvent;
import net.swofty.type.skyblockgeneric.minion.IslandMinionData;

public class ActionIslandSaveMinions implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM ,requireDataLoaded = false)
    public void run(IslandSavedIntoDatabaseEvent event) {
        IslandMinionData minionData = event.getIsland().getMinionData();
        minionData.getMinions().forEach(IslandMinionData.IslandMinion::removeMinion);

        event.getIsland().getDatabase().insertOrUpdate("minions", event.getIsland().getMinionData().serialize());
    }
}
