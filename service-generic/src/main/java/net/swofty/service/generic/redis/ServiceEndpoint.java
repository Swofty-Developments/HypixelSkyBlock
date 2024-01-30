package net.swofty.service.generic.redis;

import net.swofty.commons.impl.ServiceProxyRequest;

public interface ServiceEndpoint {
    String channel();
    String onMessage(ServiceProxyRequest message);
}
