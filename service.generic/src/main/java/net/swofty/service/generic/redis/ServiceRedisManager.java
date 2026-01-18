package net.swofty.service.generic.redis;

import net.swofty.commons.ServiceType;
import net.swofty.redisapi.api.RedisAPI;

public class ServiceRedisManager {
    public static void connect(String URI, ServiceType type) {
        RedisAPI.generateInstance(URI);
        RedisAPI.getInstance().setFilterId(type.name());
    }
}
