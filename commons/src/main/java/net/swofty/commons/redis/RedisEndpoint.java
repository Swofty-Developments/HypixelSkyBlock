package net.swofty.commons.redis;

import net.swofty.commons.ServiceType;

import java.util.UUID;

public record RedisEndpoint(RedisEndpointType type, String id) {
    public static RedisEndpoint proxy() {
        return new RedisEndpoint(RedisEndpointType.PROXY, "proxy");
    }

    public static RedisEndpoint service(ServiceType type) {
        return new RedisEndpoint(RedisEndpointType.SERVICE, type.name());
    }

    public static RedisEndpoint service(String id) {
        return new RedisEndpoint(RedisEndpointType.SERVICE, id);
    }

    public static RedisEndpoint server(UUID uuid) {
        return new RedisEndpoint(RedisEndpointType.SERVER, uuid.toString());
    }

    public static RedisEndpoint server(String uuid) {
        return new RedisEndpoint(RedisEndpointType.SERVER, uuid);
    }
}
