package net.swofty.service.generic;

import lombok.RequiredArgsConstructor;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisEnvelope;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.service.generic.redis.PingEndpoint;
import net.swofty.service.generic.redis.ServiceRedisManager;
import net.swofty.service.generic.redis.ServiceToServerManager;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class ServiceInitializer {
    private final SkyBlockService service;

    public void init() {
        Logger.info("Initializing service {}...", service.getType().name());
        ItemAttribute.registerItemAttributes();

        /**
         * Register Redis
         */
        ServiceRedisManager.connect(ConfigProvider.settings().getRedisUri(), service.getType());
        // Initialize service-to-server communication
        ServiceToServerManager.initialize(service.getType());

        List<RedisMessageHandler> endpoints = new ArrayList<>(service.getEndpoints());
        endpoints.add(new PingEndpoint());

        endpoints.forEach(endpoint -> {
            RedisProtocol protocolObject = endpoint.protocol();
            Logger.debug("Registering channel {}", protocolObject.channel());

            RedisAPI.getInstance().registerChannel(RedisChannels.protocol(protocolObject), message -> {
                // Everything after the first semicolon is the actual message
                String realMessage = message.message.substring(message.message.indexOf(";") + 1);
                RedisEnvelope envelope = RedisEnvelope.deserialize(realMessage);

                Object messageData = protocolObject.translateFromString(envelope.payload());

                Thread.startVirtualThread(() -> {
                    RedisMessageContext context = RedisMessageContext.serverToService(
                            UUID.fromString(envelope.id()),
                            envelope.from(),
                            service.getType().name(),
                            protocolObject.channel()
                    );
                    Object rawResponse = endpoint.handle(messageData, context);
                    String response = protocolObject.translateReturnToString(rawResponse);

                    RedisAPI.getInstance().publishMessage(envelope.from(),
                            ChannelRegistry.getFromName(protocolObject.channel()),
                            new RedisEnvelope(envelope.id(), service.getType().name(), response).serialize()).join();
                });
            });
        });

        RedisAPI.getInstance().startListeners();
        Logger.info("Service {} initialized!", service.getType().name());

        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
