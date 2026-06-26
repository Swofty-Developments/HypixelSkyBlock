package net.swofty.proxyapi;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisClient;

import java.util.concurrent.CompletableFuture;

public record ProxyService(ServiceType type) {
    public CompletableFuture<Boolean> isOnline() {
        return RedisClient.isServiceOnline(type);
    }

    public <T, R> CompletableFuture<R> handleRequest(T request) {
        return RedisClient.requestService(type, request);
    }

    public <T, R> CompletableFuture<R> handleRequest(RedisProtocol<T, R> protocol, T request) {
        return RedisClient.requestService(type, protocol, request);
    }
}
