package net.swofty.service.generic.redis;

import net.swofty.commons.ServiceType;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ServiceToServerManager {
    private static final Map<UUID, CompletableFuture<JSONObject>> pendingRequests = new ConcurrentHashMap<>();
    private static ServiceType currentServiceType;

    public static void initialize(ServiceType serviceType) {
        currentServiceType = serviceType;

        // Register response handler for server responses
        RedisAPI.getInstance().registerChannel("service_response", (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            UUID requestId = UUID.fromString(split[0].substring(split[0].indexOf(";") + 1));
            String response = split[1];

            CompletableFuture<JSONObject> future = pendingRequests.remove(requestId);
            if (future != null) {
                future.complete(new JSONObject(response));
            }
        });
    }

    /**
     * Send a message to a specific server
     */
    public static CompletableFuture<JSONObject> sendToServer(UUID serverUUID, FromServiceChannels channel, JSONObject message) {
        UUID requestId = UUID.randomUUID();
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        pendingRequests.put(requestId, future);

        // Set timeout
        future.orTimeout(10, TimeUnit.SECONDS).exceptionally(throwable -> {
            pendingRequests.remove(requestId);
            return new JSONObject().put("error", "timeout");
        });

        String channelName = "service_" + channel.getChannelName();
        String messageContent = currentServiceType.name() + "}=-=-={" + requestId + "}=-=-={" + message.toString();

        RedisAPI.getInstance().publishMessage(
                serverUUID.toString(),
                ChannelRegistry.getFromName(channelName),
                messageContent
        );

        return future;
    }

    /**
     * Send a message to multiple servers and collect all responses
     */
    public static CompletableFuture<Map<UUID, JSONObject>> sendToServers(List<UUID> serverUUIDs, FromServiceChannels channel, JSONObject message) {
        Map<UUID, CompletableFuture<JSONObject>> futures = new ConcurrentHashMap<>();

        for (UUID serverUUID : serverUUIDs) {
            futures.put(serverUUID, sendToServer(serverUUID, channel, message));
        }

        return CompletableFuture.allOf(futures.values().toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    Map<UUID, JSONObject> results = new ConcurrentHashMap<>();
                    futures.forEach((uuid, future) -> {
                        try {
                            results.put(uuid, future.get());
                        } catch (Exception e) {
                            results.put(uuid, new JSONObject().put("error", e.getMessage()));
                        }
                    });
                    return results;
                });
    }

    /**
     * Get player data from a specific server
     */
    public static CompletableFuture<JSONObject> getPlayerData(UUID serverUUID, UUID playerUUID, String dataKey) {
        JSONObject message = new JSONObject()
                .put("playerUUID", playerUUID.toString())
                .put("dataKey", dataKey);

        return sendToServer(serverUUID, FromServiceChannels.GET_PLAYER_DATA, message);
    }

    /**
     * Update player data on a specific server
     */
    public static CompletableFuture<JSONObject> updatePlayerData(UUID serverUUID, UUID playerUUID, String dataKey, JSONObject newData) {
        JSONObject message = new JSONObject()
                .put("playerUUID", playerUUID.toString())
                .put("dataKey", dataKey)
                .put("newData", newData);

        return sendToServer(serverUUID, FromServiceChannels.UPDATE_PLAYER_DATA, message);
    }

    /**
     * Lock player data across multiple servers (for mutex operations)
     */
    public static CompletableFuture<Map<UUID, JSONObject>> lockPlayerData(List<UUID> serverUUIDs, UUID playerUUID, String dataKey) {
        JSONObject message = new JSONObject()
                .put("playerUUID", playerUUID.toString())
                .put("dataKey", dataKey);

        return sendToServers(serverUUIDs, FromServiceChannels.LOCK_PLAYER_DATA, message);
    }

    /**
     * Unlock player data across multiple servers
     */
    public static CompletableFuture<Map<UUID, JSONObject>> unlockPlayerData(List<UUID> serverUUIDs, UUID playerUUID, String dataKey) {
        JSONObject message = new JSONObject()
                .put("playerUUID", playerUUID.toString())
                .put("dataKey", dataKey);

        return sendToServers(serverUUIDs, FromServiceChannels.UNLOCK_PLAYER_DATA, message);
    }

    /**
     * Kick players from specific GUIs (like bank interface)
     */
    public static CompletableFuture<Map<UUID, JSONObject>> kickFromGUI(List<UUID> serverUUIDs, List<UUID> playerUUIDs, String guiType) {
        JSONObject message = new JSONObject()
                .put("playerUUIDs", playerUUIDs)
                .put("guiType", guiType);

        return sendToServers(serverUUIDs, FromServiceChannels.KICK_FROM_GUI, message);
    }
}