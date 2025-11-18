package net.swofty.type.generic.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.redis.ProxyToClient;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RedisTransferredFromThisServer implements ProxyToClient {
    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.PLAYER_HAS_SWITCHED_FROM_HERE;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        if (!ProxyPlayer.waitingForTransferComplete.containsKey(uuid)) {
            return new JSONObject();
        }

        CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    CompletableFuture<Void> future = ProxyPlayer.waitingForTransferComplete.get(uuid);
                    if (future != null) {
                        future.complete(null);
                    }
                    ProxyPlayer.waitingForTransferComplete.remove(uuid);
                });

        return new JSONObject();
    }
}