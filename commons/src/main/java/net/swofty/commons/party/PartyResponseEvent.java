package net.swofty.commons.party;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.swofty.commons.party.events.response.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PartyChatMessageResponseEvent.class),
        @JsonSubTypes.Type(value = PartyDisbandResponseEvent.class),
        @JsonSubTypes.Type(value = PartyInviteExpiredResponseEvent.class),
        @JsonSubTypes.Type(value = PartyInviteResponseEvent.class),
        @JsonSubTypes.Type(value = PartyLeaderTransferResponseEvent.class),
        @JsonSubTypes.Type(value = PartyMemberDisconnectTimeoutResponseEvent.class),
        @JsonSubTypes.Type(value = PartyMemberDisconnectedResponseEvent.class),
        @JsonSubTypes.Type(value = PartyMemberJoinResponseEvent.class),
        @JsonSubTypes.Type(value = PartyMemberKickResponseEvent.class),
        @JsonSubTypes.Type(value = PartyMemberLeaveResponseEvent.class),
        @JsonSubTypes.Type(value = PartyMemberRejoinedResponseEvent.class),
        @JsonSubTypes.Type(value = PartyPlayerSwitchedServerResponseEvent.class),
        @JsonSubTypes.Type(value = PartyPromotionResponseEvent.class),
        @JsonSubTypes.Type(value = PartyWarpOverviewResponseEvent.class),
        @JsonSubTypes.Type(value = PartyWarpResponseEvent.class)
})
public abstract class PartyResponseEvent extends PartyEvent {
    public PartyResponseEvent(Party party) {
        super(party);
    }
}