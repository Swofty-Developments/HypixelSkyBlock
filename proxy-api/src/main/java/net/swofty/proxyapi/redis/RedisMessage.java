package net.swofty.proxyapi.redis;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class RedisMessage {
    private static Map<UUID, Consumer<String>> redisMessageListeners = new HashMap<>();

    public static void register(String channel) {
        RedisAPI.getInstance().registerChannel(channel, (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            String message = split[0];
            UUID uuid = UUID.fromString(split[1]);

            String messageWithoutFilter = message.substring(message.indexOf(";") + 1);

            redisMessageListeners.get(uuid).accept(messageWithoutFilter);
            redisMessageListeners.remove(uuid);
        });
    }

    public static void sendMessageToProxy(String channel, String message, Consumer<String> response) {
        UUID uuid = UUID.randomUUID();
        UUID filterID = UUID.fromString(RedisAPI.getInstance().getFilterId());
        redisMessageListeners.put(uuid, response);

        RedisAPI.getInstance().publishMessage("proxy",
                ChannelRegistry.getFromName(channel),
                message + "}=-=-={" + uuid + "}=-=-={" + filterID);
    }

    public static void sendMessageService(String serviceID, String channel, String message, Consumer<String> response) {
        UUID uuid = UUID.randomUUID();
        UUID toCallback = UUID.fromString(RedisAPI.getInstance().getFilterId());
        redisMessageListeners.put(uuid, response);

        RedisAPI.getInstance().publishMessage(serviceID,
                ChannelRegistry.getFromName(channel),
                new ServiceProxyRequest(uuid, toCallback.toString(), channel, message).toJSON().toString());
    }
}
