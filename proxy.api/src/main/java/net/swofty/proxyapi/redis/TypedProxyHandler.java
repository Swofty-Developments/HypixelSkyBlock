package net.swofty.proxyapi.redis;

import net.swofty.commons.protocol.ProtocolObject;

public interface TypedProxyHandler<T, R> {
    ProtocolObject<T, R> getProtocol();
    R onMessage(T message);
}
