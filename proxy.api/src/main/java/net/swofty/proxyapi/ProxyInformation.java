package net.swofty.proxyapi;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProxyInformation {
    public CompletableFuture<UnderstandableProxyServer> getServerInformation(UUID uuid) {
        JSONObject message = new JSONObject()
                .put("request_type", "UUID")
                .put("uuid", uuid.toString());
        CompletableFuture<UnderstandableProxyServer> future = new CompletableFuture<>();
        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.REQUEST_SERVERS, message, (response) -> {
            future.complete(UnderstandableProxyServer.fromJSON(response.getJSONObject("servers_list")).getFirst());
        });
        return future;
    }

    public CompletableFuture<List<UnderstandableProxyServer>> getServerInformation(ServerType type) {
        JSONObject message = new JSONObject()
                .put("request_type", "TYPE")
                .put("type", type.name());
        CompletableFuture<List<UnderstandableProxyServer>> future = new CompletableFuture<>();
        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.REQUEST_SERVERS, message, (response) -> {
            future.complete(UnderstandableProxyServer.fromJSON(response.getJSONObject("servers_list")));
        });
        return future;
    }

    public CompletableFuture<List<UnderstandableProxyServer>> getAllServersInformation() {
        JSONObject message = new JSONObject()
                .put("request_type", "ALL");
        CompletableFuture<List<UnderstandableProxyServer>> future = new CompletableFuture<>();
        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.REQUEST_SERVERS, message, (response) -> {
            future.complete(UnderstandableProxyServer.fromJSON(response.getJSONObject("servers_list")));
        });
        return future;
    }

    public CompletableFuture<UnderstandableProxyServer> getServerInformation(ProxyPlayer player) {
        JSONObject message = new JSONObject()
                .put("request_type", "PLAYER_UUID")
                .put("uuid", player.getUuid().toString());
        CompletableFuture<UnderstandableProxyServer> future = new CompletableFuture<>();
        ServerOutboundMessage.sendMessageToProxy(ToProxyChannels.REQUEST_SERVERS, message, (response) -> {
            future.complete(UnderstandableProxyServer.fromJSON(response.getJSONObject("servers_list")).getFirst());
        });
        return future;
    }
}
