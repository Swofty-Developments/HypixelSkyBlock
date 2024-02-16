package net.swofty.type.village.events;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.protocol.auctions.ProtocolFetchItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@EventParameters(description = "Handles fixing a players auctions dependant on DB changes, used primarily for testing",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerAuctionsFix extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerSpawnEvent event = (PlayerSpawnEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        Thread.startVirtualThread(() -> {
            if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) return;

            List<UUID> activeOwned = player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
            List<UUID> activeBids = player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).getValue();
            List<UUID> inactiveOwned = player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();
            List<UUID> inactiveBids = player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class).getValue();

            checkAuction(activeOwned);
            checkAuction(activeBids);
            checkAuction(inactiveOwned);
            checkAuction(inactiveBids);

            player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(activeOwned);
            player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).setValue(activeBids);
            player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(inactiveOwned);
            player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class).setValue(inactiveBids);
        });
    }

    private void checkAuction(List<UUID> activeBids) {
        List<UUID> activeBidsToRemove = new ArrayList<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        activeBids.parallelStream().forEach(uuid -> {
            CompletableFuture<Void> future = new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolFetchItem(), new JSONObject().put("uuid", uuid)).thenAccept(response -> {
                if (response.getString("item").equals("null")) {
                    Logger.info("Removing auction item with UUID: " + uuid.toString());
                    activeBidsToRemove.add(uuid);
                }
            });
            futures.add(future);
        });
        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allDone.join();
        activeBids.removeAll(activeBidsToRemove);
    }
}
