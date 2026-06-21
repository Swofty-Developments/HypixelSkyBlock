package net.swofty.commons.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.swofty.commons.protocol.RedisProtocol;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisChannels {
    public static final String PROXY_RESPONSE = "proxy";
    public static final String SERVICE_RESPONSE = "service_response";
    public static final String SERVICE_BROADCAST_RESPONSE = "service_broadcast_response";
    public static final String ALL_SERVERS = "all";

    public static String protocol(RedisProtocol<?, ?> protocol) {
        return protocol.channel();
    }

    public static String serviceRequest(RedisProtocol<?, ?> protocol) {
        return "service_" + protocol.channel();
    }

    public static String serviceBroadcast(RedisProtocol<?, ?> protocol) {
        return "service_broadcast_" + protocol.channel();
    }
}
