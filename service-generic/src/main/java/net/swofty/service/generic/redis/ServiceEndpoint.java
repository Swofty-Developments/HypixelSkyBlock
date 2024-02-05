package net.swofty.service.generic.redis;

import net.swofty.commons.impl.ServiceProxyRequest;
import org.json.JSONObject;

public interface ServiceEndpoint {
    String channel();
    JSONObject onMessage(ServiceProxyRequest message);
}
