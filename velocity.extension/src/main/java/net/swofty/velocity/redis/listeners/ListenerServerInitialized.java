package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.REGISTER_SERVER)
public class ListenerServerInitialized extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        ServerType type = ServerType.valueOf(message.getString("type"));
        int port = -1;
        if (message.has("port")) {
            port = message.getInt("port");
        }

        GameManager.GameServer server = GameManager.addServer(type, serverUUID, port);

        return new JSONObject().put("port", server.registeredServer().getServerInfo().getAddress().getPort());
    }
}
