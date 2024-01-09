package net.swofty.types.generic.redis;

import net.swofty.proxyapi.redis.ProxyToClient;

public class RedisPing implements ProxyToClient {
    @Override
    public String onMessage(String message) {
        return "success";
    }
}
