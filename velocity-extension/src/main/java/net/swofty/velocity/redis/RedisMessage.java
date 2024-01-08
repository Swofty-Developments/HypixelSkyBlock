package net.swofty.velocity.redis;

import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class RedisMessage {
    private static final Map<UUID, Consumer<String>> callbacks = new HashMap<>();

    public static void sendMessageToServer(UUID server, String channel, String message, Consumer<String> callback) {
        UUID requestID = UUID.randomUUID();

        callbacks.put(requestID, callback);

        RedisAPI.getInstance().publishMessage(
                server.toString(),
                ChannelRegistry.getFromName(channel),
                requestID + "}=-=-={" + message);
    }

    public static void registerProxyToServer(String channelID) {
        RedisAPI.getInstance().registerChannel(channelID, (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            String rawMessage = split[1];
            UUID request = UUID.fromString(split[0].substring(split[0].indexOf(";") + 1));

            callbacks.get(request).accept(rawMessage);
            callbacks.remove(request);
        });
    }
}
