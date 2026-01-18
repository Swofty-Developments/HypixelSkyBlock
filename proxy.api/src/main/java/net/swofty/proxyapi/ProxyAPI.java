package net.swofty.proxyapi;

import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.UUID;

public class ProxyAPI {
    private final UUID serverUUID;

    public ProxyAPI(String URI, UUID serverUUID) {
        this.serverUUID = serverUUID;

        RedisAPI.generateInstance(URI);
        RedisAPI.getInstance().setFilterId(serverUUID.toString());
    }

    public void registerFromProxyHandler(ProxyToClient handler) {
        RedisAPI.getInstance().registerChannel(handler.getChannel().getChannelName(), (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            UUID request = UUID.fromString(split[0].substring(split[0].indexOf(";") + 1));
            String rawMessage = split[1];
            JSONObject json = new JSONObject(rawMessage);

            JSONObject response = handler.onMessage(json);

            if (!handler.getChannel().matchesRequirementsServerSide(response)) {
                Logger.error("Handler " + handler.getClass().getName());
                throw new RuntimeException("Message does not match requirements for server side");
            }

            RedisAPI.getInstance().publishMessage(
                    "proxy",
                    ChannelRegistry.getFromName(handler.getChannel().getChannelName()),
                    request + "}=-=-={" + response.toString());
        });
    }

    public void registerFromServiceHandler(ServiceToClient handler) {
        RedisAPI.getInstance().registerChannel("service_" + handler.getChannel().getChannelName(), (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            String serviceId = split[0].substring(split[0].indexOf(";") + 1);
            UUID requestId = UUID.fromString(split[1]);
            String rawMessage = split[2];
            JSONObject json = new JSONObject(rawMessage);

            Thread.startVirtualThread(() -> {
                JSONObject response = handler.onMessage(json);

                // Send response back to service
                RedisAPI.getInstance().publishMessage(
                        serviceId,
                        ChannelRegistry.getFromName("service_response"),
                        requestId + "}=-=-={" + response.toString());
            });
        });

        RedisAPI.getInstance().registerChannel("service_broadcast_" + handler.getChannel().getChannelName(), (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            String serviceId = split[0].substring(split[0].indexOf(";") + 1);
            UUID requestId = UUID.fromString(split[1]);
            String rawMessage = split[2];
            JSONObject json = new JSONObject(rawMessage);

            Thread.startVirtualThread(() -> {
                // Handle message
                JSONObject response = handler.onMessage(json);

                if (response == null) {
                    // Don't send a response if null
                    return;
                }

                // Send response back to service with this server's UUID
                RedisAPI.getInstance().publishMessage(
                        serviceId,
                        ChannelRegistry.getFromName("service_broadcast_response"),
                        requestId + "}=-=-={" + serverUUID.toString() + "}=-=-={" + response.toString());
            });
        });
    }

    public void start() {
        RedisAPI.getInstance().startListeners();
    }
}
