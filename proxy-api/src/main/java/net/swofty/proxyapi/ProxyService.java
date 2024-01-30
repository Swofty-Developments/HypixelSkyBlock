package net.swofty.proxyapi;

import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.RedisMessage;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public record ProxyService(ServiceType type) {
    public CompletableFuture<Boolean> isOnline() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        JSONObject json = new JSONObject();

        AtomicBoolean hasReceivedResponse = new AtomicBoolean(false);

        RedisMessage.sendMessageService(type.name(), "isOnline", json.toString(), (s) -> {
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
}
