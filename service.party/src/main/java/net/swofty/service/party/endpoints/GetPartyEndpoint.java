package net.swofty.service.party.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.party.FullParty;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.party.GetPartyProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.party.PartyCache;

import java.util.UUID;

public class GetPartyEndpoint implements ServiceEndpoint
        <GetPartyProtocolObject.GetPartyMessage,
                GetPartyProtocolObject.GetPartyResponse> {
    @Override
    public ProtocolObject<GetPartyProtocolObject.GetPartyMessage, GetPartyProtocolObject.GetPartyResponse> associatedProtocolObject() {
        return new GetPartyProtocolObject();
    }

    @Override
    public GetPartyProtocolObject.GetPartyResponse onMessage(ServiceProxyRequest message, GetPartyProtocolObject.GetPartyMessage messageObject) {
        UUID memberUUID = messageObject.memberUuid();
        FullParty party = PartyCache.getPartyFromPlayer(memberUUID);
        if (party == null) throw new RuntimeException("Player is not in a party");
        return new GetPartyProtocolObject.GetPartyResponse(party);
    }
}
