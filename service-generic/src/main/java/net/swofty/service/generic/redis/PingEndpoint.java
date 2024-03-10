package net.swofty.service.generic.redis;

import net.swofty.commons.impl.ServiceProxyRequest;

import java.util.HashMap;
import java.util.Map;

public class PingEndpoint implements ServiceEndpoint {
    @Override
    public String channel() {
        return "service-ping";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("online", true);
        return response;
    }

}
