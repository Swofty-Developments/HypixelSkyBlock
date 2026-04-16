package net.swofty.service.generic;

import lombok.RequiredArgsConstructor;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.management.ManagementServer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.service.generic.redis.PingEndpoint;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceRedisManager;
import net.swofty.service.generic.redis.ServiceToServerManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class ServiceInitializer {
    private final SkyBlockService service;
    private final AtomicBoolean ready = new AtomicBoolean(false);

    public void init() {
        System.out.println("Initializing service " + service.getType().name() + "...");
        ItemAttribute.registerItemAttributes();
        ManagementServer.start(
            "service-" + service.getType().name().toLowerCase(),
            ConfigProvider.settings().getKubernetes(),
            () -> true,
            ready::get,
            () -> ManagementServer.metricLine(
                "hypixel_service_info",
                1,
                java.util.Map.of("service", service.getType().name())
            )
        );

        // Register Redis
        ServiceRedisManager.connect(ConfigProvider.settings().getRedisUri(), service.getType());
        // Initialize service-to-server communication
        ServiceToServerManager.initialize(service.getType());

        List<ServiceEndpoint> endpoints = new ArrayList<>(service.getEndpoints());
        endpoints.add(new PingEndpoint());

        endpoints.forEach(endpoint -> {
            ProtocolObject protocolObject = endpoint.associatedProtocolObject();
            System.out.println("Registering channel " + protocolObject.channel());

            RedisAPI.getInstance().registerChannel(protocolObject.channel(), message -> {
                // Everything after the first semicolon is the actual message
                String realMessage = message.message.substring(message.message.indexOf(";") + 1);
                ServiceProxyRequest request = ServiceProxyRequest.fromJSON(new JSONObject(realMessage));

                Object messageData = protocolObject.translateFromString(request.getMessage());

                Thread.startVirtualThread(() -> {
                    Object rawResponse = endpoint.onMessage(request, messageData);
                    if (!request.isExpectsResponse()) {
                        return;
                    }

                    String response = protocolObject.translateReturnToString(rawResponse);

                    RedisAPI.getInstance().publishMessage(request.getRequestServer(),
                            ChannelRegistry.getFromName(request.getEndpoint()),
                            request.getRequestId() + "}=-=---={" + response).join();
                });
            });
        });

        RedisAPI.getInstance().startListeners();
        ready.set(true);
        System.out.println("Service " + service.getType().name() + " initialized!");

        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
