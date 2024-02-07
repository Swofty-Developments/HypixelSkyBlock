package net.swofty.service.generic.redis;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.json.JSONObject;

public class PingEndpoint implements ServiceEndpoint {
    @Override
    public String channel() {
        return "service-ping";
    }

    @Override
    public JSONObject onMessage(ServiceProxyRequest message) {
        return new JSONObject().put("online", true);
    }
}
