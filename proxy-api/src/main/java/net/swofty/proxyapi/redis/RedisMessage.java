package net.swofty.proxyapi.redis;

import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.redisapi.api.RedisChannel;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class RedisMessage {
    private static Map<UUID, Consumer<String>> redisMessageListeners = new HashMap<>();

    public static void register(String channel) {
        RedisAPI.getInstance().registerChannel(channel, (event) -> {
            Logger.info("Received message from Redis: " + event.message);
            String[] split = event.message.split("}=-=-=\\{");
            String message = split[0];
            UUID uuid = UUID.fromString(split[1]);

            redisMessageListeners.get(uuid).accept(message);
            redisMessageListeners.remove(uuid);
        });
    }

    public static void sendMessageToProxy(String channel, String message, Consumer<String> response) {
        UUID uuid = UUID.randomUUID();
        redisMessageListeners.put(uuid, response);

        RedisAPI.getInstance().publishMessage("proxy",
                ChannelRegistry.getFromName(channel),
                message + "}=-=-={" + uuid);
    }
}
