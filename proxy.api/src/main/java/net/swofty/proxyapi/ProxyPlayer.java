package net.swofty.proxyapi;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.objects.proxy.to.PlayerHandlerProtocol;
import net.swofty.proxyapi.impl.ProxyUnderstandableEvent;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
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
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.MESSAGE,
                        Map.of("message", JSONComponentSerializer.json().serialize(message))),
                response -> {});
    }

    public void sendMessage(String message) {
        sendMessage(Component.text(message));
    }

    public void teleport(Pos pos) {
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TELEPORT,
                        Map.of("x", pos.x(), "y", pos.y(), "z", pos.z(),
                                "yaw", pos.yaw(), "pitch", pos.pitch())),
                response -> {});
    }

    public CompletableFuture<UnderstandableProxyServer> getServer() {
        CompletableFuture<UnderstandableProxyServer> future = new CompletableFuture<>();
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.GET_SERVER, Map.of()),
                response -> {
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
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.IS_ONLINE, Map.of()),
                response -> {
                    Object isOnline = response.data().get("isOnline");
                    future.complete(Boolean.TRUE.equals(isOnline));
                });
        return future;
    }

    public void runEvent(ProxyUnderstandableEvent event) {
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.EVENT,
                        Map.of("event", event.getClass().getName(),
                                "data", event.asProxyUnderstandable())),
                response -> {});
    }

    public CompletableFuture<Void> transferToWithIndication(UUID serverToTransferTo) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TRANSFER_WITH_UUID,
                        Map.of("server_uuid", serverToTransferTo.toString())),
                response -> {});
        waitingForTransferComplete.put(uuid, future);
        return future;
    }

    public void transferTo(ServerType serverType) {
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TRANSFER,
                        Map.of("type", serverType.toString())),
                response -> {});
    }

    public void transferToLimbo() {
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.LIMBO, Map.of()),
                response -> {});
    }

    public CompletableFuture<Void> transferToWithIndication(ServerType serverType) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.TRANSFER,
                        Map.of("type", serverType.toString())),
                response -> {});
        waitingForTransferComplete.put(uuid, future);
        return future;
    }

    public CompletableFuture<UUID> getBankHash() {
        CompletableFuture<UUID> future = new CompletableFuture<>();
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.BANK_HASH, Map.of()),
                response -> {
                    Object bankHash = response.data().get("bankHash");
                    future.complete(UUID.fromString((String) bankHash));
                });
        return future;
    }

    public void refreshCoopData(String datapoint) {
        ServerOutboundMessage.sendToProxy(PLAYER_HANDLER,
                new PlayerHandlerProtocol.Request(uuid.toString(), PlayerHandlerProtocol.Action.REFRESH_COOP_DATA,
                        Map.of("datapoint", datapoint)),
                response -> {});
    }
}
