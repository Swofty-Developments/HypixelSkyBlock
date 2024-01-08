package net.swofty.velocity.redis;

import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.redisapi.events.RedisMessagingReceiveInterface;

import java.util.UUID;

public abstract class RedisListener {

    public void onMessage(String channel, String message) {
        String[] split = message.split("}=-=-=\\{");
        String rawMessage = split[0];
        UUID uuid = UUID.fromString(split[1]);

        String response = receivedMessage(rawMessage);
        RedisAPI.getInstance().publishMessage(
                ChannelRegistry.getFromName(channel),
                response + "}=-=-={" + uuid);
    }

    public abstract String receivedMessage(String message);
}
