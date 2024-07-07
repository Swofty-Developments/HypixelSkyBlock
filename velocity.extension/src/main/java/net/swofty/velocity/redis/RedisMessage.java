package net.swofty.velocity.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RedisMessage {
    private static final Map<UUID, CompletableFuture<JSONObject>> callbacks = new HashMap<>();

    public static CompletableFuture<JSONObject> sendMessageToServer(UUID server,
                                                                FromProxyChannels channel,
                                                                JSONObject message) {
        UUID requestID = UUID.randomUUID();
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        callbacks.put(requestID, future);

        RedisAPI.getInstance().publishMessage(
                server.toString(),
                ChannelRegistry.getFromName(channel.getChannelName()),
                requestID + "}=-=-={" + message.toString());

        return future;
    }

    public static void registerProxyToServer(FromProxyChannels channel) {
        RedisAPI.getInstance().registerChannel(channel.getChannelName(), (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            UUID request = UUID.fromString(split[0].substring(split[0].indexOf(";") + 1));
            String rawMessage = split[1];

            try {
                callbacks.get(request).complete(new JSONObject(rawMessage));
                callbacks.remove(request);
            } catch (Exception e) {
                System.out.println("RedisMessage: Error while processing message");
                System.out.println("Channel: " + event.channel);
                System.out.println("Message: " + event.message);
            }
        });
    }
}
