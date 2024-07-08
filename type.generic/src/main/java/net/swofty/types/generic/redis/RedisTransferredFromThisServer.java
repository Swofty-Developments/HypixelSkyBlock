package net.swofty.types.generic.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.redis.ProxyToClient;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

        Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            CompletableFuture<Void> future = ProxyPlayer.waitingForTransferComplete.get(uuid);
            future.complete(null);

            ProxyPlayer.waitingForTransferComplete.remove(uuid);
        });

        return new JSONObject();
    }
}