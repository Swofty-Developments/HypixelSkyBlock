package net.swofty.service.generic.redis;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.UpdatePlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol;
import net.swofty.commons.protocol.objects.gui.KickFromGUIPushProtocol;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;

public class ServiceToServerManager {
    private static final Map<UUID, CompletableFuture<JSONObject>> pendingRequests = new ConcurrentHashMap<>();
    // Keep track of in-flight broadcasts
    private static final Map<UUID, BroadcastRequest> pendingBroadcastRequests = new ConcurrentHashMap<>();
    // Single threaded scheduler to fire timeouts
    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "broadcast-timeouter");
                t.setDaemon(true);
                return t;
            });
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

        RedisAPI.getInstance().registerChannel("service_broadcast_response", (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            UUID requestId = UUID.fromString(split[0].substring(split[0].indexOf(";") + 1));
            UUID serverUUID = UUID.fromString(split[1]);
            String response = split[2];

            BroadcastRequest broadcastRequest = pendingBroadcastRequests.get(requestId);
            if (broadcastRequest != null) {
                broadcastRequest.addResponse(serverUUID, new JSONObject(response));
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

    public static <T, R> CompletableFuture<R> sendToServer(
            UUID serverUUID,
            ServicePushProtocol<T, R> protocol,
            T message
    ) {
        UUID requestId = UUID.randomUUID();
        CompletableFuture<R> future = new CompletableFuture<>();
        CompletableFuture<JSONObject> rawFuture = new CompletableFuture<>();

        pendingRequests.put(requestId, rawFuture);

        rawFuture.orTimeout(10, TimeUnit.SECONDS).exceptionally(throwable -> {
            pendingRequests.remove(requestId);
            return null;
        });

        rawFuture.thenAccept(json -> {
            if (json == null) {
                future.completeExceptionally(new TimeoutException("Service push timed out"));
                return;
            }
            try {
                R response = protocol.translateReturnFromString(json.toString());
                future.complete(response);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        String serialized = protocol.translateToString(message);
        String channelName = "service_" + protocol.channel();
        String messageContent = currentServiceType.name() + "}=-=-={" + requestId + "}=-=-={" + serialized;

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
     * Send a message to ALL servers and collect responses
     */
    public static CompletableFuture<Map<UUID, JSONObject>> sendToAllServers(FromServiceChannels channel, JSONObject message) {
        return sendToAllServers(channel, message, 300); // Default 300ms timeout
    }

    /**
     * Send a message to all servers and collect responses for up to `timeoutMs` milliseconds.
     * When the timeout elapses, completes the future with whatever has been collected so far.
     */
    public static CompletableFuture<Map<UUID, JSONObject>> sendToAllServers(
            FromServiceChannels channel,
            JSONObject message,
            int timeoutMs
    ) {
        UUID requestId = UUID.randomUUID();
        CompletableFuture<Map<UUID, JSONObject>> future = new CompletableFuture<>();

        // Track this request
        BroadcastRequest broadcastRequest = new BroadcastRequest(future);
        pendingBroadcastRequests.put(requestId, broadcastRequest);

        // Build and publish the Redis message
        String channelName = "service_broadcast_" + channel.getChannelName();
        String messageContent = currentServiceType.name()
                + "}=-=-={" + requestId
                + "}=-=-={" + message.toString();
        RedisAPI.getInstance()
                .publishMessage("all",
                        ChannelRegistry.getFromName(channelName),
                        messageContent);

        // Schedule the timeout task
        scheduler.schedule(() -> {
            // Remove from pending and complete with collected responses
            BroadcastRequest req = pendingBroadcastRequests.remove(requestId);
            if (req != null) {
                req.getFuture().complete(req.getResponses());
            }
        }, timeoutMs, TimeUnit.MILLISECONDS);

        return future;
    }

    public static <T, R> CompletableFuture<Map<UUID, R>> sendToAllServers(
            ServicePushProtocol<T, R> protocol,
            T message,
            int timeoutMs
    ) {
        UUID requestId = UUID.randomUUID();
        CompletableFuture<Map<UUID, JSONObject>> rawFuture = new CompletableFuture<>();
        CompletableFuture<Map<UUID, R>> typedFuture = new CompletableFuture<>();

        BroadcastRequest broadcastRequest = new BroadcastRequest(rawFuture);
        pendingBroadcastRequests.put(requestId, broadcastRequest);

        String serialized = protocol.translateToString(message);
        String channelName = "service_broadcast_" + protocol.channel();
        String messageContent = currentServiceType.name()
                + "}=-=-={" + requestId
                + "}=-=-={" + serialized;

        RedisAPI.getInstance().publishMessage("all",
                ChannelRegistry.getFromName(channelName),
                messageContent);

        scheduler.schedule(() -> {
            BroadcastRequest req = pendingBroadcastRequests.remove(requestId);
            if (req != null) {
                req.getFuture().complete(req.getResponses());
            }
        }, timeoutMs, TimeUnit.MILLISECONDS);

        rawFuture.thenAccept(rawMap -> {
            Map<UUID, R> typedMap = new ConcurrentHashMap<>();
            rawMap.forEach((uuid, json) -> {
                try {
                    typedMap.put(uuid, protocol.translateReturnFromString(json.toString()));
                } catch (Exception e) {
                    System.err.println("Failed to deserialize push response from " + uuid + ": " + e.getMessage());
                }
            });
            typedFuture.complete(typedMap);
        });

        return typedFuture;
    }

    public static <T, R> CompletableFuture<Map<UUID, R>> sendToServers(
            List<UUID> serverUUIDs,
            ServicePushProtocol<T, R> protocol,
            T message
    ) {
        Map<UUID, CompletableFuture<R>> futures = new ConcurrentHashMap<>();

        for (UUID serverUUID : serverUUIDs) {
            futures.put(serverUUID, sendToServer(serverUUID, protocol, message));
        }

        return CompletableFuture.allOf(futures.values().toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    Map<UUID, R> results = new ConcurrentHashMap<>();
                    futures.forEach((uuid, future) -> {
                        try {
                            results.put(uuid, future.get());
                        } catch (Exception e) {
                            // skip failed entries
                        }
                    });
                    return results;
                });
    }

    private static final GetPlayerDataPushProtocol GET_PLAYER_DATA_PROTOCOL = new GetPlayerDataPushProtocol();
    private static final UpdatePlayerDataPushProtocol UPDATE_PLAYER_DATA_PROTOCOL = new UpdatePlayerDataPushProtocol();
    private static final LockPlayerDataPushProtocol LOCK_PLAYER_DATA_PROTOCOL = new LockPlayerDataPushProtocol();
    private static final UnlockPlayerDataPushProtocol UNLOCK_PLAYER_DATA_PROTOCOL = new UnlockPlayerDataPushProtocol();

    public static CompletableFuture<GetPlayerDataPushProtocol.Response> getPlayerData(UUID serverUUID, UUID playerUUID, String dataKey) {
        return sendToServer(serverUUID, GET_PLAYER_DATA_PROTOCOL,
                new GetPlayerDataPushProtocol.Request(playerUUID, dataKey));
    }

    public static CompletableFuture<UpdatePlayerDataPushProtocol.Response> updatePlayerData(UUID serverUUID, UUID playerUUID, String dataKey, String newData) {
        return sendToServer(serverUUID, UPDATE_PLAYER_DATA_PROTOCOL,
                new UpdatePlayerDataPushProtocol.Request(playerUUID, dataKey, newData));
    }

    public static CompletableFuture<Map<UUID, LockPlayerDataPushProtocol.Response>> lockPlayerData(List<UUID> serverUUIDs, UUID playerUUID, String dataKey) {
        return sendToServers(serverUUIDs, LOCK_PLAYER_DATA_PROTOCOL,
                new LockPlayerDataPushProtocol.Request(playerUUID, dataKey));
    }

    public static CompletableFuture<Map<UUID, UnlockPlayerDataPushProtocol.Response>> unlockPlayerData(List<UUID> serverUUIDs, UUID playerUUID, String dataKey) {
        return sendToServers(serverUUIDs, UNLOCK_PLAYER_DATA_PROTOCOL,
                new UnlockPlayerDataPushProtocol.Request(playerUUID, dataKey));
    }

    private static final KickFromGUIPushProtocol KICK_FROM_GUI_PROTOCOL = new KickFromGUIPushProtocol();
    private static final GameInformationPushProtocol GAME_INFORMATION_PROTOCOL = new GameInformationPushProtocol();

    public static CompletableFuture<Map<UUID, KickFromGUIPushProtocol.Response>> kickFromGUI(List<UUID> serverUUIDs, List<UUID> playerUUIDs, String guiType) {
        return sendToServers(serverUUIDs, KICK_FROM_GUI_PROTOCOL,
                new KickFromGUIPushProtocol.Request(playerUUIDs, guiType));
    }

    public static CompletableFuture<Map<UUID, GameInformationPushProtocol.Response>> gameInformation(UUID serverUUID, UUID playerUUID, String gameId) {
        return sendToServers(List.of(serverUUID), GAME_INFORMATION_PROTOCOL,
                new GameInformationPushProtocol.Request(playerUUID, gameId));
    }
}