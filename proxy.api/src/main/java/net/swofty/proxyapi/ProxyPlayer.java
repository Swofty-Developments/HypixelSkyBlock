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
import java.util.function.BiFunction;

@Getter
public class ProxyPlayer {
    private static final PlayerHandlerProtocol PLAYER_HANDLER = new PlayerHandlerProtocol();
    private static volatile BiFunction<UUID, UUID, CompletableFuture<String>> transferPreparation =
            (player, server) -> CompletableFuture.completedFuture(null);

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
        waitingForTransferComplete.put(uuid, future);
        transferPreparation.apply(uuid, serverToTransferTo).thenAccept(document -> RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TRANSFER_WITH_UUID,
                        document == null
                                ? Map.of("server_uuid", serverToTransferTo.toString())
                                : Map.of("server_uuid", serverToTransferTo.toString(), "document", document)))
                .thenAccept(response -> {
                    if (!response.success()) {
                        waitingForTransferComplete.remove(uuid);
                        future.completeExceptionally(new IllegalStateException(response.error()));
                        sendMessage("§cUnable to transfer you: " + response.error());
                    }
                })).exceptionally(error -> {
            waitingForTransferComplete.remove(uuid);
            future.completeExceptionally(error);
            sendMessage("§cUnable to transfer you: " + rootMessage(error));
            return null;
        });
        return future;
    }

    public void transferTo(ServerType serverType) {
        resolveServer(serverType).thenAccept(this::transferToWithIndication)
                .exceptionally(error -> {
                    sendMessage("§cUnable to transfer you: " + rootMessage(error));
                    return null;
                });
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
        return resolveServer(serverType).thenCompose(this::transferToWithIndication);
    }

    private CompletableFuture<UUID> resolveServer(ServerType serverType) {
        return RedisClient.requestProxy(PLAYER_HANDLER,
                        new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.RESOLVE_TRANSFER,
                                Map.of("type", serverType.toString())))
                .thenApply(response -> {
                    if (!response.success() || response.data().get("server_uuid") == null) {
                        throw new IllegalStateException(response.error() != null ? response.error() : "No destination server available");
                    }
                    return UUID.fromString((String) response.data().get("server_uuid"));
                });
    }

    public static void setTransferPreparation(BiFunction<UUID, UUID, CompletableFuture<String>> preparation) {
        transferPreparation = preparation;
    }

    private static String rootMessage(Throwable error) {
        Throwable cause = error;
        while (cause.getCause() != null) cause = cause.getCause();
        return cause.getMessage() != null ? cause.getMessage() : cause.getClass().getSimpleName();
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

    public void refreshCoopData(String datapoint) {
        RedisClient.requestProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.REFRESH_COOP_DATA,
                        Map.of("datapoint", datapoint)));
    }
}
