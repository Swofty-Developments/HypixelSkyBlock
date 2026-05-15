package net.swofty.velocity.redis;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisEndpoint;
import net.swofty.commons.redis.RedisEnvelope;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.tinylog.Logger;

import java.util.UUID;

public final class RedisHandlerRegistry {
    private RedisHandlerRegistry() {
    }

    public static <T, R> void register(RedisMessageHandler<T, R> handler) {
        RedisProtocol<T, R> protocol = handler.protocol();

        RedisAPI.getInstance().registerChannel(RedisChannels.protocol(protocol), event -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);
            RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
            RedisMessageContext context = RedisMessageContext.proxyToServer(
                    UUID.fromString(envelope.id()),
                    envelope.from(),
                    RedisEndpoint.proxy().id(),
                    protocol.channel()
            );

            T message = protocol.translateFromString(envelope.payload());

            Thread.startVirtualThread(() -> {
                R response;
                try {
                    response = handler.handle(message, context);
                } catch (Exception e) {
                    Logger.error(e, "Error on channel {} with message {}", event.channel, envelope.payload());
                    return;
                }

                String serializedResponse = protocol.translateReturnToString(response);
                RedisAPI.getInstance().publishMessage(
                        envelope.from(),
                        ChannelRegistry.getFromName(event.channel),
                        new RedisEnvelope(envelope.id(), RedisEndpoint.proxy().id(), serializedResponse).serialize());
            });
        });
    }
}
