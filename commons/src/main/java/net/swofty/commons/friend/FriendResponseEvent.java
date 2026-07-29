package net.swofty.commons.friend;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.swofty.commons.friend.events.response.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FriendAddedResponseEvent.class),
        @JsonSubTypes.Type(value = FriendBestToggledResponseEvent.class),
        @JsonSubTypes.Type(value = FriendDeniedResponseEvent.class),
        @JsonSubTypes.Type(value = FriendJoinNotificationEvent.class),
        @JsonSubTypes.Type(value = FriendLeaveNotificationEvent.class),
        @JsonSubTypes.Type(value = FriendListResponseEvent.class),
        @JsonSubTypes.Type(value = FriendNicknameSetResponseEvent.class),
        @JsonSubTypes.Type(value = FriendRemoveAllResponseEvent.class),
        @JsonSubTypes.Type(value = FriendRemovedResponseEvent.class),
        @JsonSubTypes.Type(value = FriendRequestExpiredResponseEvent.class),
        @JsonSubTypes.Type(value = FriendRequestReceivedResponseEvent.class),
        @JsonSubTypes.Type(value = FriendRequestSentResponseEvent.class),
        @JsonSubTypes.Type(value = FriendRequestsListResponseEvent.class),
        @JsonSubTypes.Type(value = FriendSettingToggledResponseEvent.class)
})
public abstract class FriendResponseEvent extends FriendEvent {
    public FriendResponseEvent() {
        super();
    }
}
