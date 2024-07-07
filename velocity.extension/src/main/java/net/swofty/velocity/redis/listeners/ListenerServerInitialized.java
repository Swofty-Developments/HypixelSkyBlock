package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.UUID;

@ChannelListener(channel = "registeredServer-initialized")
public class ListenerServerInitialized extends RedisListener {
    @Override
    public String receivedMessage(String message, UUID serverUUID) {
        JSONObject json = new JSONObject(message);
        ServerType type = ServerType.valueOf(json.getString("type"));
        int port = json.getInt("port");

        GameManager.GameServer server = GameManager.addServer(type, serverUUID , port);

        return String.valueOf(server.registeredServer().getServerInfo().getAddress().getPort());
    }
}
