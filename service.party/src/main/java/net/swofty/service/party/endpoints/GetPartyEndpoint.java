package net.swofty.service.party.endpoints;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.party.GetPartyProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.party.PartyCache;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class GetPartyEndpoint implements RedisMessageHandler
        <GetPartyProtocol.GetPartyMessage,
                GetPartyProtocol.GetPartyResponse> {
    @Override
    public RedisProtocol<GetPartyProtocol.GetPartyMessage, GetPartyProtocol.GetPartyResponse> protocol() {
        return new GetPartyProtocol();
    }

    @Override
    public GetPartyProtocol.GetPartyResponse handle(GetPartyProtocol.GetPartyMessage messageObject, RedisMessageContext context) {
        UUID memberUUID = messageObject.memberUuid();
        FullParty party = PartyCache.getPartyFromPlayer(memberUUID);
        // A solo player simply has no party — return an empty response instead of throwing,
        // otherwise the request never resolves and the caller times out (breaking game joins).
        if (party == null) return new GetPartyProtocol.GetPartyResponse(null, false, "Player is not in a party");
        return new GetPartyProtocol.GetPartyResponse(party, true, null);
    }
}
