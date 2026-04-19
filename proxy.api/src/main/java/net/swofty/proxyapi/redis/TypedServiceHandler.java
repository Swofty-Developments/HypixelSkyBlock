package net.swofty.proxyapi.redis;

import net.swofty.commons.protocol.ServicePushProtocol;

public interface TypedServiceHandler<T, R> {
    ServicePushProtocol<T, R> getProtocol();
    R onMessage(T message);
}
