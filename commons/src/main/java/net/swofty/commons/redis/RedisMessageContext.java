package net.swofty.commons.redis;

import java.util.UUID;

public record RedisMessageContext(
        UUID requestId,
        RedisEndpoint origin,
        RedisEndpoint destination,
        String channel,
        boolean broadcast
) {
    public static RedisMessageContext proxyToServer(UUID requestId, String proxyId, String serverId, String channel) {
        return new RedisMessageContext(
                requestId,
                RedisEndpoint.proxy(),
                RedisEndpoint.server(serverId),
                channel,
                false
        );
    }

    public static RedisMessageContext serverToProxy(UUID requestId, String serverId, String channel) {
        return new RedisMessageContext(
                requestId,
                RedisEndpoint.server(serverId),
                RedisEndpoint.proxy(),
                channel,
                false
        );
    }

    public static RedisMessageContext serverToService(UUID requestId, String serverId, String serviceId, String channel) {
        return new RedisMessageContext(
                requestId,
                RedisEndpoint.server(serverId),
                RedisEndpoint.service(serviceId),
                channel,
                false
        );
    }

    public static RedisMessageContext serviceToServer(UUID requestId, String serviceId, String serverId, String channel) {
        return new RedisMessageContext(
                requestId,
                RedisEndpoint.service(serviceId),
                RedisEndpoint.server(serverId),
                channel,
                false
        );
    }

    public RedisMessageContext asBroadcast() {
        return new RedisMessageContext(requestId, origin, destination, channel, true);
    }
}
