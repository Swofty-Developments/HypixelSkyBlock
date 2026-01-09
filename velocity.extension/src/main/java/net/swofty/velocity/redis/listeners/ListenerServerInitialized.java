package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.config.ConfigProvider;
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

        String host = message.has("host")
                ? message.getString("host")
                : ConfigProvider.settings().getHostName(); // fallback to config if not present

        int maxPlayers = message.getInt("max_players");

        GameManager.GameServer server = GameManager.addServer(type, serverUUID, host, port, maxPlayers);
        return new JSONObject()
                .put("host", server.registeredServer().getServerInfo().getAddress().getHostString())
                .put("port", server.registeredServer().getServerInfo().getAddress().getPort());
    }
}
