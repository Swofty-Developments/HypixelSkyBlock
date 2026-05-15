package net.swofty.proxyapi;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisClient;
import net.swofty.commons.redis.RedisEndpoint;
import net.swofty.commons.redis.RedisMessageBus;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.redisapi.api.RedisAPI;

import java.util.UUID;

public class ProxyAPI {
    private final UUID serverUUID;

    public ProxyAPI(String URI, UUID serverUUID) {
        this.serverUUID = serverUUID;

        RedisAPI.generateInstance(URI);
        RedisAPI.getInstance().setFilterId(serverUUID.toString());
        RedisClient.identify(RedisEndpoint.server(serverUUID));
    }

    public <T, R> void registerProxyHandler(RedisMessageHandler<T, R> handler) {
        RedisProtocol<T, R> protocol = handler.protocol();

        RedisMessageBus.registerHandler(
                RedisEndpoint.server(serverUUID),
                RedisChannels.protocol(protocol),
                handler,
                (envelope, channel) -> RedisMessageContext.between(
                        UUID.fromString(envelope.id()),
                        RedisEndpoint.proxy(),
                        RedisEndpoint.server(serverUUID),
                        protocol.channel()
                ),
                envelope -> RedisChannels.PROXY_RESPONSE,
                envelope -> RedisChannels.protocol(protocol)
        );
    }

    public <T, R> void registerServiceHandler(RedisMessageHandler<T, R> handler) {
        RedisProtocol<T, R> protocol = handler.protocol();

        RedisMessageBus.registerHandler(
                RedisEndpoint.server(serverUUID),
                RedisChannels.serviceRequest(protocol),
                handler,
                (envelope, channel) -> RedisMessageContext.between(
                        UUID.fromString(envelope.id()),
                        RedisEndpoint.service(envelope.from()),
                        RedisEndpoint.server(serverUUID),
                        protocol.channel()
                ),
                envelope -> envelope.from(),
                envelope -> RedisChannels.SERVICE_RESPONSE
        );

        RedisMessageBus.registerHandler(
                RedisEndpoint.server(serverUUID),
                RedisChannels.serviceBroadcast(protocol),
                handler,
                (envelope, channel) -> RedisMessageContext.between(
                        UUID.fromString(envelope.id()),
                        RedisEndpoint.service(envelope.from()),
                        RedisEndpoint.server(serverUUID),
                        protocol.channel()
                ).asBroadcast(),
                envelope -> envelope.from(),
                envelope -> RedisChannels.SERVICE_BROADCAST_RESPONSE
        );
    }

    public void start() {
        RedisAPI.getInstance().startListeners();
    }
}
