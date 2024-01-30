package net.swofty.service.generic.redis;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.generic.redis.ServiceEndpoint;

public class PingEndpoint implements ServiceEndpoint {
    @Override
    public String channel() {
        return "isOnline";
    }

    @Override
    public String onMessage(ServiceProxyRequest message) {
        return "pong";
    }
}
