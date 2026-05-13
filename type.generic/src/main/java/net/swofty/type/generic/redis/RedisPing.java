package net.swofty.type.generic.redis;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.from.PingServerProtocol;
import net.swofty.proxyapi.redis.TypedProxyHandler;

public class RedisPing implements TypedProxyHandler<PingServerProtocol.Request, PingServerProtocol.Response> {
    @Override
    public ProtocolObject<PingServerProtocol.Request, PingServerProtocol.Response> getProtocol() {
        return new PingServerProtocol();
    }

    @Override
    public PingServerProtocol.Response onMessage(PingServerProtocol.Request message) {
        return new PingServerProtocol.Response();
    }
}
