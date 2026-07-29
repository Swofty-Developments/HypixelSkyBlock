package net.swofty.velocity.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisEndpoint;
import net.swofty.commons.redis.RedisMessageBus;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisHandlerRegistry {

    public static <T, R> void register(RedisMessageHandler<T, R> handler) {
        RedisProtocol<T, R> protocol = handler.protocol();

        RedisMessageBus.registerHandler(
                RedisEndpoint.proxy(),
                RedisChannels.protocol(protocol),
                handler,
                (envelope, channel) -> RedisMessageContext.between(
                        UUID.fromString(envelope.id()),
                        RedisEndpoint.server(envelope.from()),
                        RedisEndpoint.proxy(),
                        protocol.channel()
                ),
                envelope -> envelope.from(),
                envelope -> RedisChannels.protocol(protocol)
        );
    }
}
