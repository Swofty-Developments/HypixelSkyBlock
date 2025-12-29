package net.swofty.service.friend.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.friend.events.*;
import net.swofty.commons.protocol.objects.friend.SendFriendEventToServiceProtocolObject;
import net.swofty.service.friend.FriendCache;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.HashMap;

public class FriendEventToServiceEndpoint implements ServiceEndpoint<
        SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage,
        SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse> {

    @Override
    public SendFriendEventToServiceProtocolObject associatedProtocolObject() {
        return new SendFriendEventToServiceProtocolObject();
    }

    @Override
    public SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse onMessage(
            ServiceProxyRequest message,
            SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage messageObject) {

        try {
            FriendEvent event = messageObject.event();

            System.out.println("Received friend event: " + event.getClass().getSimpleName());
            switch (event) {
                case FriendAddRequestEvent e -> FriendCache.handleAddRequest(e, "Player", "Player");
                case FriendAcceptRequestEvent e -> FriendCache.handleAcceptRequest(e, "Player", "Player");
                case FriendDenyRequestEvent e -> FriendCache.handleDenyRequest(e, "Player");
                case FriendRemoveRequestEvent e -> FriendCache.handleRemoveRequest(e, "Player", "Player");
                case FriendRemoveAllRequestEvent e -> FriendCache.handleRemoveAllRequest(e);
                case FriendToggleBestRequestEvent e -> FriendCache.handleToggleBestRequest(e, "Player");
                case FriendSetNicknameRequestEvent e -> FriendCache.handleSetNicknameRequest(e, "Player");
                case FriendToggleSettingRequestEvent e -> FriendCache.handleToggleSettingRequest(e);
                case FriendListRequestEvent e -> FriendCache.handleListRequest(e, new HashMap<>(), new HashMap<>());
                case FriendRequestsListEvent e -> FriendCache.handleRequestsListRequest(e, new HashMap<>());
                default -> Logger.warn("Unknown friend event type: " + event.getClass().getSimpleName());
            }

            return new SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse(true);
        } catch (Exception e) {
            System.out.println("Failed to process friend event: " + e.getMessage());
            Logger.error(e, "Failed to process friend event in service endpoint");
            return new SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse(false);
        }
    }
}
