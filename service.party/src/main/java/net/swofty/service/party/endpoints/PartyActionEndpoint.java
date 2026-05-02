package net.swofty.service.party.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.party.PartyAction;
import net.swofty.commons.protocol.objects.party.SendPartyActionProtocolObject;
import net.swofty.commons.protocol.objects.party.SendPartyActionProtocolObject.Request;
import net.swofty.commons.protocol.objects.party.SendPartyActionProtocolObject.Response;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.party.PartyCache;
import org.tinylog.Logger;

public class PartyActionEndpoint implements ServiceEndpoint<Request, Response> {

    @Override
    public SendPartyActionProtocolObject associatedProtocolObject() {
        return new SendPartyActionProtocolObject();
    }

    @Override
    public Response onMessage(ServiceProxyRequest message, Request request) {
        try {
            PartyAction action = request.action();
            switch (action) {
                case PartyAction.Invite a -> PartyCache.handleInvite(a);
                case PartyAction.AcceptInvite a -> PartyCache.handleAcceptInvite(a);
                case PartyAction.Leave a -> PartyCache.handleLeave(a);
                case PartyAction.Disband a -> PartyCache.handleDisband(a);
                case PartyAction.Transfer a -> PartyCache.handleTransfer(a);
                case PartyAction.Kick a -> PartyCache.handleKick(a);
                case PartyAction.Promote a -> PartyCache.handlePromote(a);
                case PartyAction.Demote a -> PartyCache.handleDemote(a);
                case PartyAction.Warp a -> PartyCache.handleWarp(a);
                case PartyAction.Hijack a -> PartyCache.handleHijack(a);
                case PartyAction.Chat a -> PartyCache.handleChat(a);
                case PartyAction.SwitchedServer a -> PartyCache.handleSwitchedServer(a);
                case PartyAction.PlayerDisconnect a -> PartyCache.handlePlayerDisconnect(a);
                case PartyAction.PlayerRejoin a -> PartyCache.handlePlayerRejoin(a);
            }
            return Response.success();
        } catch (Exception e) {
            Logger.error(e, "Failed to process party action");
            return Response.failure("Action processing failed: " + e.getMessage());
        }
    }
}
