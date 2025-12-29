package net.swofty.service.friend.endpoints;

import net.swofty.commons.friend.FriendData;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.friend.GetFriendDataProtocolObject;
import net.swofty.service.friend.FriendCache;
import net.swofty.service.generic.redis.ServiceEndpoint;

public class GetFriendDataEndpoint implements ServiceEndpoint<
        GetFriendDataProtocolObject.GetFriendDataMessage,
        GetFriendDataProtocolObject.GetFriendDataResponse> {

    @Override
    public GetFriendDataProtocolObject associatedProtocolObject() {
        return new GetFriendDataProtocolObject();
    }

    @Override
    public GetFriendDataProtocolObject.GetFriendDataResponse onMessage(
            ServiceProxyRequest message,
            GetFriendDataProtocolObject.GetFriendDataMessage messageObject) {

        FriendData data = FriendCache.getFriendData(messageObject.playerUuid());
        return new GetFriendDataProtocolObject.GetFriendDataResponse(data);
    }
}
