package net.swofty.type.island.events.custom;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.extension.MinionExtensionData;
import net.swofty.types.generic.minion.extension.extensions.MinionFuelExtension;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public class ActionIslandLoadMinions implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(IslandFetchedFromDatabaseEvent event) {
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
            MinionExtensionData extensionData = data.getExtensionData();

            long timeBetweenActions = (long) tier.timeBetweenActions();
            ItemTypeLinker minionFuel = extensionData.getOfType(MinionFuelExtension.class).getItemTypeLinkerPassedIn();

            //Handle percentage speed increase from both fuels and minion upgrades
            double percentageSpeedIncrease = data.getSpeedPercentage();

            // Decrease timeBetweenActions by the percentage speed increase, so if above is 300, then it's 3x faster
            timeBetweenActions = (long) (timeBetweenActions / (1 + (percentageSpeedIncrease / 100)));

            int amountOfActions = Math.round((float) (currentTime - lastSaved) / (timeBetweenActions * 1000L));

            data.spawnMinion(event.getIsland().getIslandInstance());
            if (lastSaved != 0) {
                Thread.startVirtualThread(() -> {
                    for (int i = 0; i < amountOfActions; i++) {
                        MinionAction action = data.getMinion().asSkyBlockMinion().getAction();
                        List<SkyBlockItem> items = action.onAction(
                                new MinionAction.MinionActionEvent(),
                                data,
                                event.getIsland().getIslandInstance());

                        if (!items.isEmpty())
                            MinionAction.onMinionIteration(data, data.getMinionEntity().getMinion(), items);
                    }
                });
            }
        });
    }
}
