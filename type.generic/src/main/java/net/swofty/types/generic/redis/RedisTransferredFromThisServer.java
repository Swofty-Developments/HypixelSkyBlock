package net.swofty.types.generic.redis;

import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.redis.ProxyToClient;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RedisTransferredFromThisServer implements ProxyToClient {
    @Override
    public String onMessage(String message) {
        UUID playerUuid = UUID.fromString(message);
        if (!ProxyPlayer.waitingForTransferComplete.containsKey(playerUuid)) {
            return "ok";
        }

        Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            CompletableFuture<Void> future = ProxyPlayer.waitingForTransferComplete.get(playerUuid);
            future.complete(null);

            ProxyPlayer.waitingForTransferComplete.remove(playerUuid);
        });

        return "ok";
    }
}