package net.swofty.proxyapi;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.objects.proxy.to.RequestServersProtocol;
import net.swofty.commons.redis.RedisClient;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProxyInformation {
    private static final RequestServersProtocol PROTOCOL = new RequestServersProtocol();

    public CompletableFuture<UnderstandableProxyServer> getServerInformation(UUID uuid) {
        CompletableFuture<UnderstandableProxyServer> future = new CompletableFuture<>();
        RedisClient.requestProxy(PROTOCOL,
                        new RequestServersProtocol.Request("UUID", null, uuid.toString()))
                .thenAccept(response -> future.complete(UnderstandableProxyServer.fromJSON(new JSONObject(response.serversList())).getFirst()));
        return future;
    }

    public CompletableFuture<List<UnderstandableProxyServer>> getServerInformation(ServerType type) {
        CompletableFuture<List<UnderstandableProxyServer>> future = new CompletableFuture<>();
        RedisClient.requestProxy(PROTOCOL,
                        new RequestServersProtocol.Request("TYPE", type.name(), null))
                .thenAccept(response -> future.complete(UnderstandableProxyServer.fromJSON(new JSONObject(response.serversList()))));
        return future;
    }

    public CompletableFuture<List<UnderstandableProxyServer>> getAllServersInformation() {
        CompletableFuture<List<UnderstandableProxyServer>> future = new CompletableFuture<>();
        RedisClient.requestProxy(PROTOCOL,
                        new RequestServersProtocol.Request("ALL", null, null))
                .thenAccept(response -> future.complete(UnderstandableProxyServer.fromJSON(new JSONObject(response.serversList()))));
        return future;
    }

    public CompletableFuture<UnderstandableProxyServer> getServerInformation(ProxyPlayer player) {
        CompletableFuture<UnderstandableProxyServer> future = new CompletableFuture<>();
        RedisClient.requestProxy(PROTOCOL,
                        new RequestServersProtocol.Request("PLAYER_UUID", null, player.getUuid().toString()))
                .thenAccept(response -> future.complete(UnderstandableProxyServer.fromJSON(new JSONObject(response.serversList())).getFirst()));
        return future;
    }
}
