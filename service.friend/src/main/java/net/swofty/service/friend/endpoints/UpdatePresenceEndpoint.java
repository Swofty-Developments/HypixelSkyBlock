package net.swofty.service.friend.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.presence.UpdatePresenceProtocolObject;
import net.swofty.service.friend.PresenceStorage;
import net.swofty.service.generic.redis.ServiceEndpoint;

public class UpdatePresenceEndpoint implements ServiceEndpoint<
        UpdatePresenceProtocolObject.UpdatePresenceMessage,
        UpdatePresenceProtocolObject.UpdatePresenceResponse> {

    @Override
    public UpdatePresenceProtocolObject associatedProtocolObject() {
        return new UpdatePresenceProtocolObject();
    }

    @Override
    public UpdatePresenceProtocolObject.UpdatePresenceResponse onMessage(
            ServiceProxyRequest message,
            UpdatePresenceProtocolObject.UpdatePresenceMessage messageObject) {

        PresenceStorage.upsert(messageObject.presence());
        return new UpdatePresenceProtocolObject.UpdatePresenceResponse(true);
    }
}

