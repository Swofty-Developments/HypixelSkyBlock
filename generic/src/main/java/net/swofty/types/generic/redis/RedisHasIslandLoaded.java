package net.swofty.types.generic.redis;

import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.user.SkyBlockIsland;

import java.util.UUID;

public class RedisHasIslandLoaded implements ProxyToClient {
    @Override
    public String onMessage(String message) {
        return String.valueOf(SkyBlockIsland.hasIsland(UUID.fromString(message)));
    }
}
