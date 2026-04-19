package net.swofty.velocity.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.commons.redis.RedisEnvelope;
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
                new RedisEnvelope(requestID.toString(), "proxy", message.toString()).serialize());

        return future;
    }

    public static void registerProxyToServer(FromProxyChannels channel) {
        RedisAPI.getInstance().registerChannel(channel.getChannelName(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);
            RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
            UUID request = UUID.fromString(envelope.id());
            String rawMessage = envelope.payload();

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
