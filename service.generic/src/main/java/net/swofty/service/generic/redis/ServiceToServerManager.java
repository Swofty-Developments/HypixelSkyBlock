package net.swofty.service.generic.redis;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisEndpoint;
import net.swofty.commons.redis.RedisMessageBus;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.UpdatePlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.game.GameInformationPushProtocol;
import net.swofty.commons.protocol.objects.gui.KickFromGUIPushProtocol;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceToServerManager {
    private static ServiceType currentServiceType;

    public static void initialize(ServiceType serviceType) {
        currentServiceType = serviceType;
        RedisMessageBus.registerResponseChannel(RedisChannels.SERVICE_RESPONSE);
        RedisMessageBus.registerResponseChannel(RedisChannels.SERVICE_BROADCAST_RESPONSE);
    }

    public static <T, R> CompletableFuture<R> sendToServer(
            UUID serverUUID,
            RedisProtocol<T, R> protocol,
            T message
    ) {
        return RedisMessageBus.request(
                RedisEndpoint.service(currentServiceType),
                serverUUID.toString(),
                RedisChannels.serviceRequest(protocol),
                RedisChannels.SERVICE_RESPONSE,
                protocol,
                message
        );
    }

    public static <T, R> CompletableFuture<Map<UUID, R>> sendToAllServers(
            RedisProtocol<T, R> protocol,
            T message,
            int timeoutMs
    ) {
        return RedisMessageBus.requestBroadcast(
                RedisEndpoint.service(currentServiceType),
                RedisChannels.ALL_SERVERS,
                RedisChannels.serviceBroadcast(protocol),
                RedisChannels.SERVICE_BROADCAST_RESPONSE,
                protocol,
                message,
                timeoutMs
        );
    }

    public static <T, R> CompletableFuture<Map<UUID, R>> sendToServers(
            List<UUID> serverUUIDs,
            RedisProtocol<T, R> protocol,
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
