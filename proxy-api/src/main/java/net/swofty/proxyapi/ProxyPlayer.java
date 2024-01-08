package net.swofty.proxyapi;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.RespawnPacket;
import net.minestom.server.network.packet.server.play.data.DeathLocation;
import net.minestom.server.world.DimensionTypeManager;
import net.swofty.commons.ServerType;
import net.swofty.proxyapi.redis.RedisMessage;
import org.json.JSONObject;

import java.util.UUID;

public class ProxyPlayer {
    private final UUID uuid;
    private final Player player;

    public ProxyPlayer(Player player) {
        this.uuid = player.getUuid();
        this.player = player;
    }

    public void transferTo(ServerType serverType) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("action", "transfer");
        json.put("type", serverType.toString());

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});
    }
}
