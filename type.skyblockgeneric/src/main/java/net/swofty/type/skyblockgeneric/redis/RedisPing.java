package net.swofty.type.skyblockgeneric.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import org.json.JSONObject;

public class RedisPing implements ProxyToClient {
    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.PING_SERVER;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        return new JSONObject();
    }
}
