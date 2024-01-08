package net.swofty.velocity.redis.listeners;

import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;

@ChannelListener(channel = "server-initialized")
public class ListenerServerInitialized extends RedisListener {

    @Override
    public String receivedMessage(String message) {
        System.out.println(message);
        return "pog";
    }
}
