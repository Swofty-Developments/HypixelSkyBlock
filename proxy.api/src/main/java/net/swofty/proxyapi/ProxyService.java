package net.swofty.proxyapi;

import net.swofty.commons.ServiceType;
import net.swofty.commons.redis.RedisClient;

import java.util.concurrent.CompletableFuture;

public record ProxyService(ServiceType type) {
    public CompletableFuture<Boolean> isOnline() {
        return RedisClient.isServiceOnline(type);
    }

    public <T, R> CompletableFuture<R> handleRequest(T request) {
        return RedisClient.requestService(type, request);
    }
}
