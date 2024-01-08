package net.swofty.proxyapi;

import net.swofty.commons.ServerType;
import net.swofty.proxyapi.redis.RedisMessage;
import org.json.JSONObject;

import java.util.UUID;

public class ProxyPlayer {
    private final UUID uuid;

    public ProxyPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void transferTo(ServerType serverType) {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("action", "transfer");
        json.put("type", serverType.toString());

        RedisMessage.sendMessageToProxy("player-handler", json.toString(), (s) -> {});
    }
}
