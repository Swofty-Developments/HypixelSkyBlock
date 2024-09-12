package net.swofty.proxyapi;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.PingProtocolObject;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
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

        Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!hasReceivedResponse.get()) {
                future.complete(false);
            }
        });

        return future;
    }

    public <T, R> CompletableFuture<R> handleRequest(T request) {
        ProtocolObject<T, R> protocolObject = ServerOutboundMessage.protocolObjects.get(request.getClass().getSimpleName());

        CompletableFuture<R> future = new CompletableFuture<>();
        System.out.println("Sending message to service " + type.name() + "... " + protocolObject.channel());
        Thread.startVirtualThread(() -> {
            ServerOutboundMessage.sendMessageToService(type, protocolObject, request, (s) -> {
                System.out.println("Received message from service " + type.name() + "...");
                Thread.startVirtualThread(() -> {
                    future.complete(protocolObject.translateReturnFromString(s));
                });
            });
        });
        return future;
    }
}
