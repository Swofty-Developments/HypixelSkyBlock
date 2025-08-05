package net.swofty.types.generic.data;

import net.swofty.commons.ServiceType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.objects.datamutex.SynchronizeDataProtocolObject;
import net.swofty.commons.protocol.objects.datamutex.UpdateSynchronizedDataProtocolObject;
import net.swofty.commons.protocol.objects.datamutex.UnlockDataProtocolObject;
import net.swofty.proxyapi.ProxyInformation;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DataMutexService {
    private final ProxyService service;
    private final ProxyInformation proxyInfo;

    public DataMutexService() {
        this.service = new ProxyService(ServiceType.DATA_MUTEX);
        this.proxyInfo = new ProxyInformation();
    }

    /**
     * Performs a synchronized operation on shared data across multiple servers
     *
     * @param lockKey The key to identify what data to lock (e.g., "bank_data:coop_id")
     * @param coopMembers List of coop member UUIDs
     * @param operation Function that receives the latest data and returns modified data (null = no changes)
     * @param onFailure Callback if the operation fails
     */
    public <T> void withSynchronizedData(String lockKey, List<UUID> coopMembers,
                                         DataHandler.Data dataType,
                                         Function<T, T> operation,
                                         Runnable onFailure) {

        Logger.info("Starting withSynchronizedData for lockKey: " + lockKey + ", dataType: " + dataType.getKey());

        if (!service.isOnline().join()) {
            Logger.error("DataMutexService is offline!");
            onFailure.run();
            return;
        }

        Logger.info("DataMutexService is online, getting servers for players: " + coopMembers);

        // Get list of servers where coop members are online
        getOnlineServersForPlayers(coopMembers).thenAccept(onlineServers -> {
            Logger.info("Found online servers: " + onlineServers);

            if (onlineServers.isEmpty()) {
                Logger.error("No online servers found for players: " + coopMembers);
                onFailure.run();
                return;
            }

            // Pick the first player for data synchronization (all coop members share the same data)
            UUID playerUUID = coopMembers.getFirst();
            Logger.info("Using playerUUID: " + playerUUID + " for synchronization");

            SynchronizeDataProtocolObject.SynchronizeDataRequest request =
                    new SynchronizeDataProtocolObject.SynchronizeDataRequest(onlineServers, playerUUID, dataType.getKey());

            Logger.info("Sending synchronization request to service...");

            CompletableFuture<SynchronizeDataProtocolObject.SynchronizeDataResponse> syncFuture =
                    service.handleRequest(request);

            syncFuture.thenAccept(response -> {
                Logger.info("Received synchronization response: success=" + response.success() + ", message=" + response.message());

                if (!response.success()) {
                    Logger.error("Failed to synchronize data: " + response.message());
                    onFailure.run();
                    return;
                }

                try {
                    Logger.info("Attempting to deserialize data...");

                    // Deserialize the synchronized data
                    T currentData = (T) dataType.getDefaultDatapoint().getSerializer()
                            .deserialize(response.synchronizedData());

                    Logger.info("Successfully deserialized data, applying operation...");

                    // Apply the operation
                    T modifiedData = operation.apply(currentData);

                    Logger.info("Operation completed, modifiedData is null: " + (modifiedData == null));

                    if (modifiedData != null) {
                        Logger.info("Serializing modified data for update...");

                        // Serialize and update across all servers
                        String serializedData = dataType.getDefaultDatapoint().getSerializer()
                                .serialize(modifiedData);

                        UpdateSynchronizedDataProtocolObject.UpdateDataRequest updateRequest =
                                new UpdateSynchronizedDataProtocolObject.UpdateDataRequest(
                                        onlineServers, playerUUID, dataType.getKey(), serializedData);

                        CompletableFuture<UpdateSynchronizedDataProtocolObject.UpdateDataResponse> updateFuture =
                                service.handleRequest(updateRequest);

                        updateFuture.thenAccept(updateResponse -> {
                            Logger.info("Update response: success=" + updateResponse.success() + ", message=" + updateResponse.message());

                            if (!updateResponse.success()) {
                                // If update fails, unlock the data
                                Logger.error("Failed to update data: " + updateResponse.message());
                                unlockData(onlineServers, playerUUID, dataType.getKey());
                                onFailure.run();
                            } else {
                                Logger.info("Data successfully synchronized across all servers!");
                            }
                            // Success - data has been synchronized across all servers
                        }).exceptionally(updateThrowable -> {
                            Logger.error("Exception during update: " + updateThrowable.getMessage(), updateThrowable);
                            unlockData(onlineServers, playerUUID, dataType.getKey());
                            onFailure.run();
                            return null;
                        });
                    } else {
                        Logger.info("No changes needed, unlocking data...");
                        // No changes needed, just unlock
                        unlockData(onlineServers, playerUUID, dataType.getKey());
                    }
                } catch (Exception e) {
                    Logger.error("Exception during data processing: " + e.getMessage(), e);
                    unlockData(onlineServers, playerUUID, dataType.getKey());
                    onFailure.run();
                }
            }).exceptionally(throwable -> {
                Logger.error("Exception during synchronization request: " + throwable.getMessage(), throwable);
                onFailure.run();
                return null;
            });
        }).exceptionally(throwable -> {
            Logger.error("Exception getting online servers: " + throwable.getMessage(), throwable);
            onFailure.run();
            return null;
        });
    }

    private CompletableFuture<List<UUID>> getOnlineServersForPlayers(List<UUID> playerUUIDs) {
        Logger.info("Getting online servers for players: " + playerUUIDs);

        List<CompletableFuture<UnderstandableProxyServer>> futures = new ArrayList<>();

        for (UUID playerUUID : playerUUIDs) {
            ProxyPlayer proxyPlayer = new ProxyPlayer(playerUUID);
            futures.add(proxyInfo.getServerInformation(proxyPlayer));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    List<UUID> onlineServers = new ArrayList<>();

                    for (int i = 0; i < futures.size(); i++) {
                        UUID playerUUID = playerUUIDs.get(i);
                        CompletableFuture<UnderstandableProxyServer> future = futures.get(i);

                        try {
                            UnderstandableProxyServer server = future.get();
                            Logger.info("Player " + playerUUID + " is on server: " + (server != null ? server.uuid() : "null"));

                            if (server != null && !onlineServers.contains(server.uuid())) {
                                onlineServers.add(server.uuid());
                            }
                        } catch (Exception e) {
                            Logger.warn("Failed to get server info for player " + playerUUID + ": " + e.getMessage());
                            // Player might be offline or proxy unreachable
                            continue;
                        }
                    }

                    Logger.info("Final online servers list: " + onlineServers);
                    return onlineServers;
                });
    }

    private void unlockData(List<UUID> serverUUIDs, UUID playerUUID, String dataKey) {
        Logger.info("Unlocking data for player " + playerUUID + " on servers: " + serverUUIDs);

        // Send unlock request to the mutex service, which will then unlock on all servers
        service.handleRequest(new UnlockDataProtocolObject.UnlockDataRequest(
                serverUUIDs, playerUUID, dataKey
        )).thenAccept(response -> {
            UnlockDataProtocolObject.UnlockDataResponse responseObject = (UnlockDataProtocolObject.UnlockDataResponse) response;
            Logger.info("Unlock response: success=" + responseObject.success() + ", message=" + responseObject.message());

            if (!responseObject.success()) {
                Logger.error("Failed to unlock data for player " + playerUUID +
                        ", dataKey: " + dataKey + " - " + responseObject.message());
            }
        }).exceptionally(throwable -> {
            Logger.error("Error unlocking data: " + throwable.getMessage(), throwable);
            return null;
        });
    }
}