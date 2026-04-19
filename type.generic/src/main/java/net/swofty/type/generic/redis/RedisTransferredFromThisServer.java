package net.swofty.type.generic.redis;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.from.PlayerSwitchedProtocol;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.redis.TypedProxyHandler;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RedisTransferredFromThisServer implements TypedProxyHandler<PlayerSwitchedProtocol.Request, PlayerSwitchedProtocol.Response> {
    @Override
    public ProtocolObject<PlayerSwitchedProtocol.Request, PlayerSwitchedProtocol.Response> getProtocol() {
        return new PlayerSwitchedProtocol();
    }

    @Override
    public PlayerSwitchedProtocol.Response onMessage(PlayerSwitchedProtocol.Request message) {
        UUID uuid = UUID.fromString(message.uuid());
        if (!ProxyPlayer.waitingForTransferComplete.containsKey(uuid)) {
            return new PlayerSwitchedProtocol.Response();
        }

        CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    CompletableFuture<Void> future = ProxyPlayer.waitingForTransferComplete.get(uuid);
                    if (future != null) {
                        future.complete(null);
                    }
                    ProxyPlayer.waitingForTransferComplete.remove(uuid);
                });

        return new PlayerSwitchedProtocol.Response();
    }
}
