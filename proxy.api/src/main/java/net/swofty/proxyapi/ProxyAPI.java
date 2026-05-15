package net.swofty.proxyapi;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisEnvelope;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;

import java.util.UUID;

public class ProxyAPI {
    private final UUID serverUUID;

    public ProxyAPI(String URI, UUID serverUUID) {
        this.serverUUID = serverUUID;

        RedisAPI.generateInstance(URI);
        RedisAPI.getInstance().setFilterId(serverUUID.toString());
    }

    public <T, R> void registerProxyHandler(RedisMessageHandler<T, R> handler) {
        RedisProtocol<T, R> protocol = handler.protocol();

        RedisAPI.getInstance().registerChannel(RedisChannels.protocol(protocol), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);
            RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
            String rawMessage = envelope.payload();

            T typedMessage = protocol.translateFromString(rawMessage);
            RedisMessageContext context = RedisMessageContext.proxyToServer(
                    UUID.fromString(envelope.id()),
                    envelope.from(),
                    serverUUID.toString(),
                    protocol.channel()
            );
            R response = handler.handle(typedMessage, context);

            String serializedResponse = protocol.translateReturnToString(response);

            RedisAPI.getInstance().publishMessage(
                    RedisChannels.PROXY_RESPONSE,
                    ChannelRegistry.getFromName(RedisChannels.protocol(protocol)),
                    new RedisEnvelope(envelope.id(), serverUUID.toString(), serializedResponse).serialize());
        });
    }

    public <T, R> void registerServiceHandler(RedisMessageHandler<T, R> handler) {
        RedisProtocol<T, R> protocol = handler.protocol();
        String channelName = RedisChannels.serviceRequest(protocol);

        RedisAPI.getInstance().registerChannel(channelName, (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);
            RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
            String serviceId = envelope.from();
            String rawMessage = envelope.payload();

            Thread.startVirtualThread(() -> {
                T typedMessage = protocol.translateFromString(rawMessage);
                RedisMessageContext context = RedisMessageContext.serviceToServer(
                        UUID.fromString(envelope.id()),
                        serviceId,
                        serverUUID.toString(),
                        protocol.channel()
                );
                R response = handler.handle(typedMessage, context);

                String serializedResponse = protocol.translateReturnToString(response);

                RedisAPI.getInstance().publishMessage(
                        serviceId,
                        ChannelRegistry.getFromName(RedisChannels.SERVICE_RESPONSE),
                        new RedisEnvelope(envelope.id(), serverUUID.toString(), serializedResponse).serialize());
            });
        });

        RedisAPI.getInstance().registerChannel(RedisChannels.serviceBroadcast(protocol), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);
            RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
            String serviceId = envelope.from();
            String rawMessage = envelope.payload();

            Thread.startVirtualThread(() -> {
                T typedMessage = protocol.translateFromString(rawMessage);
                RedisMessageContext context = RedisMessageContext.serviceToServer(
                        UUID.fromString(envelope.id()),
                        serviceId,
                        serverUUID.toString(),
                        protocol.channel()
                ).asBroadcast();
                R response = handler.handle(typedMessage, context);

                if (response == null) return;

                String serializedResponse = protocol.translateReturnToString(response);

                RedisAPI.getInstance().publishMessage(
                        serviceId,
                        ChannelRegistry.getFromName(RedisChannels.SERVICE_BROADCAST_RESPONSE),
                        new RedisEnvelope(envelope.id(), serverUUID.toString(), serializedResponse).serialize());
            });
        });
    }

    public void start() {
        RedisAPI.getInstance().startListeners();
    }
}
