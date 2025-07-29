package net.swofty.velocity.redis.listeners;

import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.REQUEST_SERVERS_NAME)
public class ListenerServerName extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        GameManager.GameServer server = GameManager.getFromUUID(serverUUID);

        return new JSONObject()
                .put("server-name", server.displayName())
                .put("shortened-server-name", server.shortDisplayName());
    }
}