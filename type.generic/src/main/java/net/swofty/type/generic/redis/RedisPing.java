package net.swofty.type.generic.redis;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.from.PingServerProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;

public class RedisPing implements RedisMessageHandler<PingServerProtocol.Request, PingServerProtocol.Response> {
    @Override
    public RedisProtocol<PingServerProtocol.Request, PingServerProtocol.Response> protocol() {
        return new PingServerProtocol();
    }

    @Override
    public PingServerProtocol.Response handle(PingServerProtocol.Request message, RedisMessageContext context) {
        return new PingServerProtocol.Response();
    }
}
