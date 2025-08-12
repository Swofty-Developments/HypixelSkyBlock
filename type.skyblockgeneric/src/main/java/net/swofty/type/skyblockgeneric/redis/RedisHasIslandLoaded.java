package net.swofty.type.skyblockgeneric.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.type.generic.user.SkyBlockIsland;
import org.json.JSONObject;

import java.util.UUID;

public class RedisHasIslandLoaded implements ProxyToClient {

    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.DOES_SERVER_HAVE_ISLAND;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID islandUUID = UUID.fromString(message.getString("island-uuid"));
        return new JSONObject().put("server-has-it", SkyBlockIsland.hasIsland(islandUUID));
    }
}
