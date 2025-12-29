package net.swofty.service.friend.endpoints;

import net.swofty.commons.friend.PendingFriendRequest;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.friend.GetPendingFriendRequestsProtocolObject;
import net.swofty.service.friend.FriendCache;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class GetPendingRequestsEndpoint implements ServiceEndpoint<
        GetPendingFriendRequestsProtocolObject.GetPendingRequestsMessage,
        GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse> {

    @Override
    public GetPendingFriendRequestsProtocolObject associatedProtocolObject() {
        return new GetPendingFriendRequestsProtocolObject();
    }

    @Override
    public GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse onMessage(
            ServiceProxyRequest message,
            GetPendingFriendRequestsProtocolObject.GetPendingRequestsMessage messageObject) {

        List<PendingFriendRequest> requests = FriendCache.getPendingRequestsFor(messageObject.playerUuid());
        return new GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse(requests);
    }
}
