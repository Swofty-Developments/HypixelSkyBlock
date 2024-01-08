package net.swofty.velocity.redis.listeners;

import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;

import java.util.UUID;

@ChannelListener(channel = "proxy-online")
public class ListenerProxyOnline extends RedisListener {
    @Override
    public String receivedMessage(String message, UUID serverUUID) {
        return "true";
    }
}
