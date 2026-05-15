package net.swofty.service.friend.endpoints;

import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.objects.presence.GetPresenceBulkProtocol;
import net.swofty.service.friend.PresenceStorage;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;
import net.swofty.commons.redis.RedisMessageContext;

public class GetPresenceEndpoint implements RedisMessageHandler<
        GetPresenceBulkProtocol.GetPresenceBulkMessage,
        GetPresenceBulkProtocol.GetPresenceBulkResponse> {

    @Override
    public GetPresenceBulkProtocol protocol() {
        return new GetPresenceBulkProtocol();
    }

    @Override
    public GetPresenceBulkProtocol.GetPresenceBulkResponse handle(GetPresenceBulkProtocol.GetPresenceBulkMessage messageObject, RedisMessageContext context) {

        List<PresenceInfo> presence = PresenceStorage.getBulk(messageObject.uuids());
        return new GetPresenceBulkProtocol.GetPresenceBulkResponse(presence, true, null);
    }
}

