package net.swofty.proxyapi.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import org.json.JSONObject;

public interface ProxyToClient {
    FromProxyChannels getChannel();
    JSONObject onMessage(JSONObject message);
}
