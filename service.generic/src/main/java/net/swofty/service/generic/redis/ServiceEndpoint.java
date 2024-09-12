package net.swofty.service.generic.redis;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;

import java.util.Map;

public interface ServiceEndpoint<T, R> {
    ProtocolObject<T, R> associatedProtocolObject();
    R onMessage(ServiceProxyRequest message, T messageObject);
}
