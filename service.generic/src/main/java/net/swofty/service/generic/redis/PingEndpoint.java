package net.swofty.service.generic.redis;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.PingProtocolObject;

import java.util.HashMap;
import java.util.Map;

public class PingEndpoint implements ServiceEndpoint<
        PingProtocolObject.EmptyMessage,
        PingProtocolObject.EmptyMessage> {
    @Override
    public ProtocolObject<PingProtocolObject.EmptyMessage, PingProtocolObject.EmptyMessage> associatedProtocolObject() {
        return new PingProtocolObject();
    }

    @Override
    public PingProtocolObject.EmptyMessage onMessage(ServiceProxyRequest message, PingProtocolObject.EmptyMessage messageObject) {
        return new PingProtocolObject.EmptyMessage();
    }
}
