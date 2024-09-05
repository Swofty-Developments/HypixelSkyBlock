package net.swofty.proxyapi;

import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.commons.protocol.ProtocolSpecification;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public record ProxyService(ServiceType type) {
    public CompletableFuture<Boolean> isOnline(ProtocolSpecification pingProtocol) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        AtomicBoolean hasReceivedResponse = new AtomicBoolean(false);

        ServerOutboundMessage.sendMessageToService(type, pingProtocol,
                new JSONObject(), (s) -> {
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

    public CompletableFuture<Map<String, Object>> callEndpoint(
            ProtocolSpecification protocol,
            Map<String, Object> values) {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();

        JSONObject json = protocol.toJSON(values, true);

        ServerOutboundMessage.sendMessageToService(type, protocol,
                json, (s) -> {
            Map<String, Object> response = protocol.fromJSON(s, false);

            Thread.startVirtualThread(() -> {
                future.complete(response);
            });
        });

        return future;
    }
}
