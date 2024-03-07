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
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
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

        long lastSaved = event.getIsland().getLastSaved();
        long currentTime = System.currentTimeMillis();

        minionData.getMinions().forEach((data) -> {
            int tierIndex = data.getTier();
            SkyBlockMinion.MinionTier tier = data.getMinion().asSkyBlockMinion().getTiers().get(tierIndex - 1);

            long timeBetweenActions = tier.timeBetweenActions();
            int amountOfActions = (int) ((currentTime - lastSaved) / timeBetweenActions);

            if (lastSaved != 0)
                Thread.startVirtualThread(() -> {
                    data.spawnMinion(event.getIsland().getIslandInstance());
                    for (int i = 0; i < amountOfActions; i++) {
                        MinionAction action = data.getMinion().asSkyBlockMinion().getAction();
                        SkyBlockItem item = action.onAction(
                                new MinionAction.MinionActionEvent(),
                                data,
                                event.getIsland().getIslandInstance());

                        if (item != null)
                            MinionAction.onMinionIteration(data, data.getMinionEntity().getMinion(), item);
                    }
                });

            minionData.spawn(data);
        });
    }
}
