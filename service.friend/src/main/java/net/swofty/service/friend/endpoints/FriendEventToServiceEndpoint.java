package net.swofty.service.friend.endpoints;

import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.friend.events.*;
import net.swofty.commons.protocol.objects.friend.SendFriendEventToServiceProtocol;
import net.swofty.service.friend.FriendCache;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;
import net.swofty.commons.redis.RedisMessageContext;

public class FriendEventToServiceEndpoint implements RedisMessageHandler<
        SendFriendEventToServiceProtocol.SendFriendEventToServiceMessage,
        SendFriendEventToServiceProtocol.SendFriendEventToServiceResponse> {

    @Override
    public SendFriendEventToServiceProtocol protocol() {
        return new SendFriendEventToServiceProtocol();
    }

    @Override
    public SendFriendEventToServiceProtocol.SendFriendEventToServiceResponse handle(SendFriendEventToServiceProtocol.SendFriendEventToServiceMessage messageObject, RedisMessageContext context) {

        try {
            FriendEvent event = messageObject.event();

            Logger.debug("Received friend event: {}", event.getClass().getSimpleName());
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

            return new SendFriendEventToServiceProtocol.SendFriendEventToServiceResponse(true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to process friend event in service endpoint");
            return new SendFriendEventToServiceProtocol.SendFriendEventToServiceResponse(false, "Event processing failed");
        }
    }
}
