package net.swofty.velocity.redis;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisEndpoint;
import net.swofty.commons.redis.RedisMessageBus;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RedisMessage {
    public static <T, R> CompletableFuture<R> sendMessageToServer(UUID server,
                                                                    RedisProtocol<T, R> protocol,
                                                                    T message) {
        return RedisMessageBus.request(
                RedisEndpoint.proxy(),
                server.toString(),
                RedisChannels.protocol(protocol),
                RedisChannels.protocol(protocol),
                protocol,
                message
        );
    }

    public static void registerProxyToServer(RedisProtocol<?, ?> protocol) {
        RedisMessageBus.registerResponseChannel(RedisChannels.protocol(protocol));
    }
}
