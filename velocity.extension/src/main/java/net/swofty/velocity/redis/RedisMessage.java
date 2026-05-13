package net.swofty.velocity.redis;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.redis.RedisEnvelope;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RedisMessage {
    private static final Map<UUID, CompletableFuture<String>> callbacks = new HashMap<>();

    public static <T, R> CompletableFuture<R> sendMessageToServer(UUID server,
                                                                    ProtocolObject<T, R> protocol,
                                                                    T message) {
        UUID requestID = UUID.randomUUID();
        CompletableFuture<String> rawFuture = new CompletableFuture<>();

        callbacks.put(requestID, rawFuture);

        String serialized = protocol.translateToString(message);
        RedisAPI.getInstance().publishMessage(
                server.toString(),
                ChannelRegistry.getFromName(protocol.channel()),
                new RedisEnvelope(requestID.toString(), "proxy", serialized).serialize());

        return rawFuture.thenApply(protocol::translateReturnFromString);
    }

    public static void registerProxyToServer(ProtocolObject<?, ?> protocol) {
        RedisAPI.getInstance().registerChannel(protocol.channel(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);
            RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
            UUID request = UUID.fromString(envelope.id());
            String rawMessage = envelope.payload();

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
