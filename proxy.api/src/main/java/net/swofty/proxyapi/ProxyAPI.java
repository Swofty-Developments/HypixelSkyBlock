package net.swofty.proxyapi;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.TypedServiceHandler;
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

    public <T, R> void registerTypedServiceHandler(TypedServiceHandler<T, R> handler) {
        ServicePushProtocol<T, R> protocol = handler.getProtocol();
        String channelName = "service_" + protocol.channel();

        RedisAPI.getInstance().registerChannel(channelName, (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            String serviceId = split[0].substring(split[0].indexOf(";") + 1);
            UUID requestId = UUID.fromString(split[1]);
            String rawMessage = split[2];

            Thread.startVirtualThread(() -> {
                T typedMessage = protocol.translateFromString(rawMessage);
                R response = handler.onMessage(typedMessage);

                String serializedResponse = protocol.translateReturnToString(response);

                RedisAPI.getInstance().publishMessage(
                        serviceId,
                        ChannelRegistry.getFromName("service_response"),
                        requestId + "}=-=-={" + serializedResponse);
            });
        });

        RedisAPI.getInstance().registerChannel("service_broadcast_" + protocol.channel(), (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            String serviceId = split[0].substring(split[0].indexOf(";") + 1);
            UUID requestId = UUID.fromString(split[1]);
            String rawMessage = split[2];

            Thread.startVirtualThread(() -> {
                T typedMessage = protocol.translateFromString(rawMessage);
                R response = handler.onMessage(typedMessage);

                if (response == null) return;

                String serializedResponse = protocol.translateReturnToString(response);

                RedisAPI.getInstance().publishMessage(
                        serviceId,
                        ChannelRegistry.getFromName("service_broadcast_response"),
                        requestId + "}=-=-={" + serverUUID.toString() + "}=-=-={" + serializedResponse);
            });
        });
    }

    public void start() {
        RedisAPI.getInstance().startListeners();
    }
}
