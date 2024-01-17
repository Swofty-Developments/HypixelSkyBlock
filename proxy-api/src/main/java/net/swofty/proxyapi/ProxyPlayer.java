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
import net.minestom.server.network.packet.server.play.RespawnPacket;
import net.minestom.server.network.packet.server.play.data.DeathLocation;
import net.minestom.server.world.DimensionTypeManager;
import net.swofty.commons.ServerType;
import net.swofty.proxyapi.impl.ProxyUnderstandableEvent;
import net.swofty.proxyapi.redis.RedisMessage;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class ProxyPlayer {
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

    public void refreshCoopData(String datapoint) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("actions", "refresh-coop-data");
        json.put("datapoint", datapoint);

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});
    }
}
