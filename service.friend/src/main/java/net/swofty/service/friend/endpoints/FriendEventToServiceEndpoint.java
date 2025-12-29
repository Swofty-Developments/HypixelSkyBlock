package net.swofty.service.friend.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.friend.events.*;
import net.swofty.commons.protocol.objects.friend.SendFriendEventToServiceProtocolObject;
import net.swofty.service.friend.FriendCache;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

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
                case FriendAddRequestEvent e -> FriendCache.handleAddRequest(
                        e,
                        FriendCache.getPlayerName(e.getSender()),
                        FriendCache.getPlayerName(e.getTarget())
                );
                case FriendAcceptRequestEvent e -> FriendCache.handleAcceptRequest(
                        e,
                        FriendCache.getPlayerName(e.getAccepter()),
                        FriendCache.getPlayerName(e.getRequester())
                );
                case FriendDenyRequestEvent e -> FriendCache.handleDenyRequest(
                        e,
                        FriendCache.getPlayerName(e.getDenier())
                );
                case FriendRemoveRequestEvent e -> FriendCache.handleRemoveRequest(
                        e,
                        FriendCache.getPlayerName(e.getRemover()),
                        FriendCache.getPlayerName(e.getTarget())
                );
                case FriendRemoveAllRequestEvent e -> FriendCache.handleRemoveAllRequest(e);
                case FriendToggleBestRequestEvent e -> FriendCache.handleToggleBestRequest(
                        e,
                        FriendCache.getPlayerName(e.getTarget())
                );
                case FriendSetNicknameRequestEvent e -> FriendCache.handleSetNicknameRequest(
                        e,
                        FriendCache.getPlayerName(e.getTarget())
                );
                case FriendToggleSettingRequestEvent e -> FriendCache.handleToggleSettingRequest(e);
                case FriendListRequestEvent e -> FriendCache.handleListRequest(e);
                case FriendRequestsListEvent e -> FriendCache.handleRequestsListRequest(e);
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
