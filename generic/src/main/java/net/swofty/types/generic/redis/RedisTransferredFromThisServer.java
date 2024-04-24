package net.swofty.types.generic.redis;

import net.minestom.server.event.Event;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.lang.reflect.InvocationTargetException;
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