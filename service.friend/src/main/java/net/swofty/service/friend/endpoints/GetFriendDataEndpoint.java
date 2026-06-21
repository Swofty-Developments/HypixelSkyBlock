package net.swofty.service.friend.endpoints;

import net.swofty.commons.friend.FriendData;
import net.swofty.commons.protocol.objects.friend.GetFriendDataProtocol;
import net.swofty.service.friend.FriendCache;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;

public class GetFriendDataEndpoint implements RedisMessageHandler<
        GetFriendDataProtocol.GetFriendDataMessage,
        GetFriendDataProtocol.GetFriendDataResponse> {

    @Override
    public GetFriendDataProtocol protocol() {
        return new GetFriendDataProtocol();
    }

    @Override
    public GetFriendDataProtocol.GetFriendDataResponse handle(GetFriendDataProtocol.GetFriendDataMessage messageObject, RedisMessageContext context) {

        FriendData data = FriendCache.getFriendData(messageObject.playerUuid());
        return new GetFriendDataProtocol.GetFriendDataResponse(data, true, null);
    }
}
