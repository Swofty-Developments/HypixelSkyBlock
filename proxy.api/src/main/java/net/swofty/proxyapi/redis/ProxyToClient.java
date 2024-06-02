package net.swofty.proxyapi.redis;

public interface ProxyToClient {
    String onMessage(String message);
}
