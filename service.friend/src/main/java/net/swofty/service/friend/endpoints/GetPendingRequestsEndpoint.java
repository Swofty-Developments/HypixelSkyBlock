package net.swofty.service.friend.endpoints;

import net.swofty.commons.friend.PendingFriendRequest;
import net.swofty.commons.protocol.objects.friend.GetPendingFriendRequestsProtocol;
import net.swofty.service.friend.FriendCache;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;
import net.swofty.commons.redis.RedisMessageContext;

public class GetPendingRequestsEndpoint implements RedisMessageHandler<
        GetPendingFriendRequestsProtocol.GetPendingRequestsMessage,
        GetPendingFriendRequestsProtocol.GetPendingRequestsResponse> {

    @Override
    public GetPendingFriendRequestsProtocol protocol() {
        return new GetPendingFriendRequestsProtocol();
    }

    @Override
    public GetPendingFriendRequestsProtocol.GetPendingRequestsResponse handle(GetPendingFriendRequestsProtocol.GetPendingRequestsMessage messageObject, RedisMessageContext context) {

        List<PendingFriendRequest> requests = FriendCache.getPendingRequestsFor(messageObject.playerUuid());
        return new GetPendingFriendRequestsProtocol.GetPendingRequestsResponse(requests, true, null);
    }
}
