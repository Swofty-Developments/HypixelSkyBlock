package net.swofty.service.friend.endpoints;

import net.swofty.commons.protocol.objects.friend.AreFriendsProtocol;
import net.swofty.service.friend.FriendCache;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;

public class AreFriendsEndpoint implements RedisMessageHandler<
        AreFriendsProtocol.AreFriendsMessage,
        AreFriendsProtocol.AreFriendsResponse> {

    @Override
    public AreFriendsProtocol protocol() {
        return new AreFriendsProtocol();
    }

    @Override
    public AreFriendsProtocol.AreFriendsResponse handle(AreFriendsProtocol.AreFriendsMessage messageObject, RedisMessageContext context) {

        boolean areFriends = FriendCache.areFriends(messageObject.player(), messageObject.other());
        return new AreFriendsProtocol.AreFriendsResponse(areFriends, true, null);
    }
}
