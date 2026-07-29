package net.swofty.commons.redis;

import net.swofty.commons.protocol.RedisProtocol;

public interface RedisMessageHandler<T, R> {
    RedisProtocol<T, R> protocol();

    R handle(T message, RedisMessageContext context);
}
