package net.swofty.proxyapi;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.swofty.commons.MinecraftVersion;
import net.swofty.commons.ServerType;
import net.swofty.proxyapi.impl.ProxyUnderstandableEvent;
import net.swofty.proxyapi.redis.RedisMessage;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

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

    public void sendMessage(TextComponent message) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "message");
        json.put("message", JSONComponentSerializer.json().serialize(message));

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});
    }

    public void sendMessage(String message) {
        sendMessage(Component.text(message));
    }


    public void teleport(Pos pos) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "teleport");
        json.put("x", pos.x());
        json.put("y", pos.y());
        json.put("z", pos.z());
        json.put("yaw", pos.yaw());
        json.put("pitch", pos.pitch());

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});
    }

    public CompletableFuture<Boolean> isOnline() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "N/A");

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {
            if (s.equals("true")) {
                future.complete(true);
            } else {
                future.complete(false);
            }
        });

        return future;
    }

    public void runEvent(ProxyUnderstandableEvent event) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "event");
        json.put("event", event.getClass().getName());
        json.put("data", event.asProxyUnderstandable());

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});
    }

    public void transferTo(ServerType serverType) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "transfer");
        json.put("type", serverType.toString());

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});
    }

    public CompletableFuture<Void> transferToWithIndication(ServerType serverType) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "transfer");
        json.put("type", serverType.toString());

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});

        waitingForTransferComplete.put(uuid, future);

        return future;
    }

    public CompletableFuture<UUID> getBankHash() {
        CompletableFuture<UUID> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "bank-hash");

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {
            if (s.equals("false")) {
                future.complete(null);
                return;
            }
            future.complete(UUID.fromString(s));
        });

        return future;
    }

    public CompletableFuture<MinecraftVersion> getVersion() {
        CompletableFuture<MinecraftVersion> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "version");

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {
            future.complete(MinecraftVersion.byProtocolId(Integer.parseInt(s)));
        });

        return future;
    }

    public void refreshCoopData(String datapoint) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "refresh-coop-data");
        json.put("datapoint", datapoint);

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});
    }
}
