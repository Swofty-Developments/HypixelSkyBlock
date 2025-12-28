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
    public CompletableFuture<Boolean> isOnline() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        AtomicBoolean hasReceivedResponse = new AtomicBoolean(false);

        ServerOutboundMessage.sendMessageToService(type, new PingProtocolObject(),
                new PingProtocolObject.EmptyMessage(), (s) -> {
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
        ProtocolObject<T, R> protocolObject = ServerOutboundMessage.protocolObjects.get(request.getClass().getSimpleName());

        CompletableFuture<R> future = new CompletableFuture<>();
        Thread.startVirtualThread(() -> {
            ServerOutboundMessage.sendMessageToService(type, protocolObject, request, (s) -> {
                Thread.startVirtualThread(() -> {
                    future.complete(protocolObject.translateReturnFromString(s));
                });
            });
        });
        return future;
    }
}
