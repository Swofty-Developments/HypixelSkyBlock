package net.swofty.type.island.events.custom;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.utility.MathUtility;
import org.bson.Document;

import java.util.Map;

@EventParameters(description = "Handles loading Minions on the players Island",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class ActionIslandLoadMinions extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandFetchedFromDatabaseEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        IslandFetchedFromDatabaseEvent event = (IslandFetchedFromDatabaseEvent) tempEvent;
        Document document = event.getIsland().getDatabase().getDocument();

        if (document == null) {
            event.getIsland().setMinionData(new IslandMinionData(event.getIsland()));
            return;
        }

        Map<String, Object> rawData = (Map<String, Object>) document.get("minions");
        IslandMinionData minionData = IslandMinionData.deserialize(
                rawData,
                event.getIsland()
        );

        event.getIsland().setMinionData(minionData);
        minionData.getMinions().forEach(minionData::spawn);
    }
}
