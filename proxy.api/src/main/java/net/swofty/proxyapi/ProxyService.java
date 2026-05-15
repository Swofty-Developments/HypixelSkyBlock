package net.swofty.proxyapi;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.PingProtocol;
import net.swofty.proxyapi.redis.ServerOutboundMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public record ProxyService(ServiceType type) {
    public CompletableFuture<Boolean> isOnline() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        AtomicBoolean hasReceivedResponse = new AtomicBoolean(false);

        ServerOutboundMessage.sendMessageToService(type, new PingProtocol(),
            new PingProtocol.EmptyMessage(), (s) -> {
                future.complete(true);
                hasReceivedResponse.set(true);
            });

        CompletableFuture.delayedExecutor(150, TimeUnit.MILLISECONDS)
            .execute(() -> {
                if (!hasReceivedResponse.get()) {
                    future.complete(false);
                }
            });

        return future;
    }

    public <T, R> CompletableFuture<R> handleRequest(T request) {
        RedisProtocol<T, R> protocol = ServerOutboundMessage.protocols.get(request.getClass().getSimpleName());

        CompletableFuture<R> future = new CompletableFuture<>();
        Thread.startVirtualThread(() ->
            ServerOutboundMessage.sendMessageToService(
                type,
                protocol,
                request,
                (s) -> Thread.startVirtualThread(
                    () -> future.complete(protocol.translateReturnFromString(s))
                )
            ));
        return future;
    }
}
