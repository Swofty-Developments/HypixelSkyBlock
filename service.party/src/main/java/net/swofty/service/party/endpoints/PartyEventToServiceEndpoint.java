package net.swofty.service.party.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.events.*;
import net.swofty.commons.protocol.objects.party.SendPartyEventToServiceProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.party.PartyCache;
import org.tinylog.Logger;

public class PartyEventToServiceEndpoint implements ServiceEndpoint<
        SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage,
        SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse> {

    @Override
    public SendPartyEventToServiceProtocolObject associatedProtocolObject() {
        return new SendPartyEventToServiceProtocolObject();
    }

    @Override
    public SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse onMessage(
            ServiceProxyRequest message,
            SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage messageObject) {

        try {
            PartyEvent event = messageObject.event();

            System.out.println("Received party event: " + event.getClass().getSimpleName());
            switch (event) {
                case PartyInviteEvent inviteEvent -> PartyCache.handleInviteEvent(inviteEvent);
                case PartyAcceptInviteEvent acceptEvent -> PartyCache.handleAcceptInvite(acceptEvent);
                case PartyLeaveRequestEvent leaveEvent -> PartyCache.handleLeaveRequest(leaveEvent);
                case PartyDisbandRequestEvent disbandEvent -> PartyCache.handleDisbandRequest(disbandEvent);
                case PartyTransferRequestEvent transferEvent -> PartyCache.handleTransferRequest(transferEvent);
                case PartyKickRequestEvent kickEvent -> PartyCache.handleKickRequest(kickEvent);
                case PartyPromoteRequestEvent promoteEvent -> PartyCache.handlePromoteRequest(promoteEvent);
                case PartyDemoteRequestEvent demoteEvent -> PartyCache.handleDemoteRequest(demoteEvent);
                case PartyWarpRequestEvent warpEvent -> PartyCache.handleWarpRequest(warpEvent);
                case PartyHijackRequestEvent hijackEvent -> PartyCache.handleHijackRequest(hijackEvent);
                case PartyPlayerSwitchedServerEvent switchEvent -> PartyCache.handlePlayerSwitchedServer(switchEvent);
                case PartyChatMessageEvent chatEvent -> PartyCache.handleChatMessage(chatEvent);
                case PartyPlayerDisconnectEvent disconnectEvent -> PartyCache.handlePlayerDisconnect(disconnectEvent);
                case PartyPlayerRejoinEvent rejoinEvent -> PartyCache.handlePlayerRejoin(rejoinEvent);
                default -> Logger.warn("Unknown party event type: " + event.getClass().getSimpleName());
            }

            return new SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse(true);
        } catch (Exception e) {
            System.out.println("Failed to process party event: " + e.getMessage());
            Logger.error(e, "Failed to process party event in service endpoint");
            return new SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse(false);
        }
    }
}