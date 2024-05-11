package net.swofty.velocity.redis;

import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RedisMessage {
    private static final Map<UUID, CompletableFuture<String>> callbacks = new HashMap<>();

    public static CompletableFuture<String> sendMessageToServer(UUID server, String channel, String message) {
        UUID requestID = UUID.randomUUID();
        CompletableFuture<String> future = new CompletableFuture<>();

        callbacks.put(requestID, future);

        RedisAPI.getInstance().publishMessage(
                server.toString(),
                ChannelRegistry.getFromName(channel),
                requestID + "}=-=-={" + message);

        return future;
    }

    public static void registerProxyToServer(String channelID) {
        RedisAPI.getInstance().registerChannel(channelID, (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            UUID request = UUID.fromString(split[0].substring(split[0].indexOf(";") + 1));
            String rawMessage = split[1];

            try {
                callbacks.get(request).complete(rawMessage);
                callbacks.remove(request);
            } catch (Exception e) {
                System.out.println("RedisMessage: Error while processing message");
                System.out.println("Channel: " + event.channel);
                System.out.println("Message: " + event.message);
            }
        });
    }
}
