package net.swofty.service.party.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.party.IsPlayerInPartyProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.party.PartyCache;

import java.util.UUID;

public class IsPlayerInPartyEndpoint implements ServiceEndpoint<
        IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage,
        IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse> {

    @Override
    public IsPlayerInPartyProtocolObject associatedProtocolObject() {
        return new IsPlayerInPartyProtocolObject();
    }

    @Override
    public IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse onMessage(ServiceProxyRequest message, IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage messageObject) {
        UUID playerUUID = messageObject.playerUUID();

        boolean isInParty = PartyCache.isInParty(playerUUID);

        return new IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse(isInParty);
    }
}