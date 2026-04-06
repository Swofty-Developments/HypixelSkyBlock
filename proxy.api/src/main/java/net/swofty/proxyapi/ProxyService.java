package net.swofty.proxyapi;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.PingProtocolObject;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import org.tinylog.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public record ProxyService(ServiceType type) {
    private static final long REDIS_READY_TIMEOUT_MS = 5000;
    private static final long SERVICE_PING_TIMEOUT_MS = 2000;

    public CompletableFuture<Boolean> isOnline() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        AtomicBoolean hasReceivedResponse = new AtomicBoolean(false);

        if (!awaitRedisReady()) {
            future.complete(false);
            return future;
        }

        try {
            ServerOutboundMessage.sendMessageToService(type, new PingProtocolObject(),
                    new PingProtocolObject.EmptyMessage(), (s) -> {
                future.complete(true);
                hasReceivedResponse.set(true);
            });
        } catch (Exception exception) {
            future.complete(false);
            return future;
        }

        CompletableFuture.delayedExecutor(SERVICE_PING_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    if (!hasReceivedResponse.get()) {
                        future.complete(false);
                    }
                });

        return future;
    }

    public <T, R> CompletableFuture<R> handleRequest(T request) {
        ProtocolObject<T, R> protocolObject = ServerOutboundMessage.protocolObjects.get(request.getClass().getSimpleName());

        CompletableFuture<R> future = new CompletableFuture<>();
        if (!awaitRedisReady()) {
            future.completeExceptionally(new IllegalStateException("Redis API is not ready"));
            return future;
        }

        Thread.startVirtualThread(() -> {
            try {
                ServerOutboundMessage.sendMessageToService(type, protocolObject, request, (s) -> {
                    Thread.startVirtualThread(() -> {
                        future.complete(protocolObject.translateReturnFromString(s));
                    });
                });
            } catch (Exception exception) {
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    private boolean awaitRedisReady() {
        long deadline = System.currentTimeMillis() + REDIS_READY_TIMEOUT_MS;

        while (System.currentTimeMillis() < deadline) {
            try {
                if (net.swofty.redisapi.api.RedisAPI.getInstance() != null
                        && net.swofty.redisapi.api.RedisAPI.getInstance().getFilterId() != null) {
                    return true;
                }
            } catch (Exception ignored) {
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return false;
    }
}
