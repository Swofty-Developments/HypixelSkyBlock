package net.swofty.service.friend.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.objects.presence.GetPresenceBulkProtocolObject;
import net.swofty.service.friend.PresenceStorage;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class GetPresenceEndpoint implements ServiceEndpoint<
        GetPresenceBulkProtocolObject.GetPresenceBulkMessage,
        GetPresenceBulkProtocolObject.GetPresenceBulkResponse> {

    @Override
    public GetPresenceBulkProtocolObject associatedProtocolObject() {
        return new GetPresenceBulkProtocolObject();
    }

    @Override
    public GetPresenceBulkProtocolObject.GetPresenceBulkResponse onMessage(
            ServiceProxyRequest message,
            GetPresenceBulkProtocolObject.GetPresenceBulkMessage messageObject) {

        List<PresenceInfo> presence = PresenceStorage.getBulk(messageObject.uuids());
        return new GetPresenceBulkProtocolObject.GetPresenceBulkResponse(presence);
    }
}

