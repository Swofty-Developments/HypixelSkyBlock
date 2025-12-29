package net.swofty.service.friend.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.friend.AreFriendsProtocolObject;
import net.swofty.service.friend.FriendCache;
import net.swofty.service.generic.redis.ServiceEndpoint;

public class AreFriendsEndpoint implements ServiceEndpoint<
        AreFriendsProtocolObject.AreFriendsMessage,
        AreFriendsProtocolObject.AreFriendsResponse> {

    @Override
    public AreFriendsProtocolObject associatedProtocolObject() {
        return new AreFriendsProtocolObject();
    }

    @Override
    public AreFriendsProtocolObject.AreFriendsResponse onMessage(
            ServiceProxyRequest message,
            AreFriendsProtocolObject.AreFriendsMessage messageObject) {

        boolean areFriends = FriendCache.areFriends(messageObject.player1(), messageObject.player2());
        return new AreFriendsProtocolObject.AreFriendsResponse(areFriends);
    }
}
