package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.UUID;

@ChannelListener(channel = "server-name")
public class ListenerServerName extends RedisListener {
    @Override
    public String receivedMessage(String message, UUID serverUUID) {
        GameManager.GameServer server = GameManager.getFromUUID(serverUUID);
        return server.displayName();
    }
}