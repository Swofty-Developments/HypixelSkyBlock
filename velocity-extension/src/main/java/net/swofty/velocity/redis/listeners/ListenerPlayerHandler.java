package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.UUID;

@ChannelListener(channel = "player-handler")
public class ListenerPlayerHandler extends RedisListener {
    @Override
    public String receivedMessage(String message, UUID serverUUID) {
        JSONObject json = new JSONObject(message);
        UUID uuid = UUID.fromString(json.getString("uuid"));
        String action = json.getString("action");

        Player player = SkyBlockVelocity.getServer().getPlayer(uuid).get();

        switch (action) {
            case "transfer" -> {
                ServerType type = ServerType.valueOf(json.getString("type"));
                player.createConnectionRequest(GameManager.getFromType(type).get(0).server()).fireAndForget();
            }
        }

        return "true";
    }
}
