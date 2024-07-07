package net.swofty.velocity.redis.listeners;

import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.PROXY_IS_ONLINE)
public class ListenerProxyOnline extends RedisListener {
    @Override
    public String receivedMessage(String message, UUID serverUUID) {
        if (GameManager.getFromUUID(serverUUID) == null) {
            return "false";
        }

        return "true";
    }

    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        return null;
    }
}
