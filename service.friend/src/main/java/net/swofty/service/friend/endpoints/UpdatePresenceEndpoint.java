package net.swofty.service.friend.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.objects.presence.UpdatePresenceProtocolObject;
import net.swofty.service.friend.FriendCache;
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

        return new UpdatePresenceProtocolObject.UpdatePresenceResponse(true);
    }
}

