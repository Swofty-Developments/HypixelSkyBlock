package net.swofty.proxyapi;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.commons.proxy.requirements.to.PlayerHandlerRequirements;
import net.swofty.proxyapi.impl.ProxyUnderstandableEvent;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ProxyPlayer {
    public static Map<UUID, CompletableFuture<Void>> waitingForTransferComplete = new ConcurrentHashMap<>();
    private final UUID uuid;

    public ProxyPlayer(Player player) {
        this.uuid = player.getUuid();
    }

    public ProxyPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void sendMessage(Component message) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("message", JSONComponentSerializer.json().serialize(message));

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.MESSAGE;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {});
    }

    public void sendMessage(String message) {
        sendMessage(Component.text(message));
    }


    public void teleport(Pos pos) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("x", pos.x());
        json.put("y", pos.y());
        json.put("z", pos.z());
        json.put("yaw", pos.yaw());
        json.put("pitch", pos.pitch());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.TELEPORT;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {});
    }

    public CompletableFuture<UnderstandableProxyServer> getServer() {
        CompletableFuture<UnderstandableProxyServer> future = new CompletableFuture<>();
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.GET_SERVER;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {
            future.complete(UnderstandableProxyServer.singleFromJSON(s.getJSONObject("server")));
        });

        return future;
    }

    public CompletableFuture<Boolean> isOnline() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.IS_ONLINE;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {
            boolean isOnline = (boolean) s.get("isOnline");
            future.complete(isOnline);
        });

        return future;
    }

    public void runEvent(ProxyUnderstandableEvent event) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("event", event.getClass().getName());
        json.put("data", event.asProxyUnderstandable());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.EVENT;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {});
    }

    public CompletableFuture<Void> transferToWithIndication(UUID serverToTransferTo) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("server_uuid", serverToTransferTo.toString());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.TRANSFER_WITH_UUID;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {});

        waitingForTransferComplete.put(uuid, future);

        return future;
    }

    public void transferTo(ServerType serverType) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("type", serverType.toString());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.TRANSFER;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {});
    }

    public void transferToLimbo() {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.LIMBO;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {});
    }

    public CompletableFuture<Void> transferToWithIndication(ServerType serverType) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("type", serverType.toString());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.TRANSFER;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {});

        waitingForTransferComplete.put(uuid, future);

        return future;
    }

    public CompletableFuture<UUID> getBankHash() {
        CompletableFuture<UUID> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.BANK_HASH;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {
            future.complete(UUID.fromString((String) s.get("bankHash")));
        });

        return future;
    }

    public void refreshCoopData(String datapoint) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("datapoint", datapoint);

        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.REFRESH_COOP_DATA;
        json.put("action", action.name());

        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.PLAYER_HANDLER,
                json, (s) -> {});
    }
}
