package net.swofty.service.generic.redis;

import net.swofty.commons.impl.ServiceProxyRequest;

import java.util.Map;

public interface ServiceEndpoint {
    String channel();
    Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData);
}
