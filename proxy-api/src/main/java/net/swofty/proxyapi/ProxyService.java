package net.swofty.proxyapi;

import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.RedisMessage;
import net.swofty.service.generic.PingProtocolSpecification;
import net.swofty.service.generic.ProtocolSpecification;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public record ProxyService(ServiceType type) {
    public CompletableFuture<Boolean> isOnline() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        AtomicBoolean hasReceivedResponse = new AtomicBoolean(false);

        RedisMessage.sendMessageService(type, new PingProtocolSpecification(),
                new JSONObject(), (s) -> {
            future.complete(true);
            hasReceivedResponse.set(true);
        });

        Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!hasReceivedResponse.get()) {
                future.complete(false);
            }
        });

        return future;
    }

    public CompletableFuture<JSONObject> callEndpoint(
            ProtocolSpecification protocol,
            JSONObject json) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        RedisMessage.sendMessageService(type, protocol,
                json, (s) -> {
            Thread.startVirtualThread(() -> {
                future.complete(new JSONObject(s));
            });
        });

        return future;
    }
}
