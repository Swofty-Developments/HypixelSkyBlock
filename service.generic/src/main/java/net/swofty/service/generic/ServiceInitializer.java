package net.swofty.service.generic;

import lombok.RequiredArgsConstructor;
import net.swofty.commons.Configuration;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.service.generic.redis.PingEndpoint;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceRedisManager;
import net.swofty.service.protocol.ProtocolSpecification;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ServiceInitializer {
    private final SkyBlockService service;
    public Map<String, ProtocolSpecification> cachedProtocols = new HashMap<>();

    public void init() {
        System.out.println("Initializing service " + service.getType().name() + "...");

        ItemAttribute.registerItemAttributes();

        /**
         * Cache all protocols
         */
        service.loopThroughPackage("net.swofty.types.generic.protocol",
                        ProtocolSpecification.class)
                .forEach(protocol -> cachedProtocols.put(protocol.getEndpoint(), protocol));

        /**
         * Register Redis
         */
        ServiceRedisManager.connect(Configuration.get("redis-uri"), service.getType());
        List<ServiceEndpoint> endpoints = new ArrayList<>(service.getEndpoints());
        endpoints.add(new PingEndpoint());

        endpoints.forEach(endpoint -> {
            System.out.println("Registering endpoint " + endpoint.channel() + "...");
            RedisAPI.getInstance().registerChannel(endpoint.channel(), message -> {
                String realMessage = message.message.split(";")[1];

                ServiceProxyRequest request = ServiceProxyRequest.fromJSON(new JSONObject(realMessage));
                ProtocolSpecification specification = cachedProtocols.get(request.getEndpoint());
                Map<String, Object> messageData = specification.fromJSON(
                        new JSONObject(request.getMessage()), true);

                Thread.startVirtualThread(() -> {
                    Map<String, Object> rawResponse = endpoint.onMessage(request, messageData);
                    JSONObject response = specification.toJSON(rawResponse, false);

                    request.getRequiredKeys().forEach(key -> {
                        if (!response.has(key) && !key.isEmpty()) {
                            throw new RuntimeException("Channel response " + endpoint.channel() + " does not contain required key " + key);
                        }
                    });

                    // Clear keys not in response keys
                    List<String> keysToRemove = new ArrayList<>();
                    response.keySet().forEach(key -> {
                        if (!request.getRequiredKeys().contains(key)) {
                            keysToRemove.add(key);
                        }
                    });
                    keysToRemove.forEach(response::remove);

                    RedisAPI.getInstance().publishMessage(request.getRequestServer(),
                            ChannelRegistry.getFromName(request.getEndpoint()),
                            request.getRequestId() + "}=-=-={" + response);
                });
            });
        });

        RedisAPI.getInstance().startListeners();
        System.out.println("Service " + service.getType().name() + " initialized!");
    }
}
