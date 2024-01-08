package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.UUID;

@ChannelListener(channel = "server-initialized")
public class ListenerServerInitialized extends RedisListener {

    @Override
    public String receivedMessage(String message, UUID serverUUID) {
        JSONObject json = new JSONObject(message);
        ServerType type = ServerType.valueOf(json.getString("type"));

        GameManager.GameServer server = GameManager.addServer(type, serverUUID);

        return String.valueOf(server.server().getServerInfo().getAddress().getPort());
    }
}
