package net.swofty.proxyapi;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.objects.proxy.to.PlayerHandlerProtocol;
import net.swofty.commons.redis.RedisClient;
import net.swofty.proxyapi.impl.ProxyUnderstandableEvent;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ProxyPlayer {
    private static final PlayerHandlerProtocol PLAYER_HANDLER = new PlayerHandlerProtocol();

    public static Map<UUID, CompletableFuture<Void>> waitingForTransferComplete = new ConcurrentHashMap<>();
    private final UUID uuid;

    public ProxyPlayer(Player player) {
        this.uuid = player.getUuid();
    }

    public ProxyPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void sendMessage(Component message) {
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.MESSAGE,
                        Map.of("message", JSONComponentSerializer.json().serialize(message))));
    }

    public void sendMessage(String message) {
        sendMessage(Component.text(message));
    }

    public void teleport(Pos pos) {
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TELEPORT,
                        Map.of("x", pos.x(), "y", pos.y(), "z", pos.z(),
                                "yaw", pos.yaw(), "pitch", pos.pitch())));
    }

    public CompletableFuture<UnderstandableProxyServer> getServer() {
        CompletableFuture<UnderstandableProxyServer> future = new CompletableFuture<>();
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.GET_SERVER, Map.of()))
                .thenAccept(response -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> serverMap = (Map<String, Object>) response.data().get("server");
                    if (serverMap != null) {
                        future.complete(UnderstandableProxyServer.singleFromJSON(new JSONObject(serverMap)));
                    } else {
                        future.complete(null);
                    }
                });
        return future;
    }

    public CompletableFuture<Boolean> isOnline() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.IS_ONLINE, Map.of()))
                .thenAccept(response -> {
                    Object isOnline = response.data().get("isOnline");
                    future.complete(Boolean.TRUE.equals(isOnline));
                });
        return future;
    }

    public void runEvent(ProxyUnderstandableEvent event) {
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.EVENT,
                        Map.of("event", event.getClass().getName(),
                                "data", event.asProxyUnderstandable())));
    }

    public CompletableFuture<Void> transferToWithIndication(UUID serverToTransferTo) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TRANSFER_WITH_UUID,
                        Map.of("server_uuid", serverToTransferTo.toString())));
        waitingForTransferComplete.put(uuid, future);
        return future;
    }

    public void transferTo(ServerType serverType) {
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TRANSFER,
                        Map.of("type", serverType.toString())));
    }

    public void transferToLimbo() {
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.LIMBO, Map.of()));
    }

    public void transferToLimboFromAfk(ServerType originType) {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("reason", "AFK");
        if (originType != null) {
            data.put("origin-type", originType.name());
        }

        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.LIMBO, data));
    }

    public CompletableFuture<Void> transferToWithIndication(ServerType serverType) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TRANSFER,
                        Map.of("type", serverType.toString())));
        waitingForTransferComplete.put(uuid, future);
        return future;
    }

    public CompletableFuture<UUID> getBankHash() {
        CompletableFuture<UUID> future = new CompletableFuture<>();
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.BANK_HASH, Map.of()))
                .thenAccept(response -> {
                    Object bankHash = response.data().get("bankHash");
                    future.complete(UUID.fromString((String) bankHash));
                });
        return future;
    }

}
