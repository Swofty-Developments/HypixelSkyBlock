package net.swofty.service.generic;

import net.swofty.commons.Configuration;
import net.swofty.service.generic.redis.ServiceRedisManager;

public record ServiceInitializer(SkyBlockService service) {
    public void init() {
        System.out.println("Initializing service " + service.getType().name() + "...");

        /**
         * Register Redis
         */
        ServiceRedisManager.connect(Configuration.get("redis-uri"), service.getType());
    }
}
