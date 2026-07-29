package net.swofty.service.friend.endpoints;

import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.objects.presence.UpdatePresenceProtocol;
import net.swofty.service.friend.FriendCache;
import net.swofty.service.friend.PresenceStorage;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;

public class UpdatePresenceEndpoint implements RedisMessageHandler<
        UpdatePresenceProtocol.UpdatePresenceMessage,
        UpdatePresenceProtocol.UpdatePresenceResponse> {

    @Override
    public UpdatePresenceProtocol protocol() {
        return new UpdatePresenceProtocol();
    }

    @Override
    public UpdatePresenceProtocol.UpdatePresenceResponse handle(UpdatePresenceProtocol.UpdatePresenceMessage messageObject, RedisMessageContext context) {

        PresenceInfo incoming = messageObject.presence();
        PresenceInfo previous = PresenceStorage.upsertPreservingServer(incoming);

        boolean stateChanged = previous == null || previous.isOnline() != incoming.isOnline();

        if (stateChanged) {
            String playerName = FriendCache.getPlayerName(incoming.getUuid());
            if (incoming.isOnline()) {
                FriendCache.handlePlayerJoin(incoming.getUuid(), playerName);
            } else {
                FriendCache.handlePlayerLeave(incoming.getUuid(), playerName);
            }
        }

        return new UpdatePresenceProtocol.UpdatePresenceResponse(true, null);
    }
}

