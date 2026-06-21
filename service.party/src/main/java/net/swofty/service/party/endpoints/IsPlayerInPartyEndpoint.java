package net.swofty.service.party.endpoints;

import net.swofty.commons.protocol.objects.party.IsPlayerInPartyProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.party.PartyCache;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class IsPlayerInPartyEndpoint implements RedisMessageHandler<
        IsPlayerInPartyProtocol.IsPlayerInPartyMessage,
        IsPlayerInPartyProtocol.IsPlayerInPartyResponse> {

    @Override
    public IsPlayerInPartyProtocol protocol() {
        return new IsPlayerInPartyProtocol();
    }

    @Override
    public IsPlayerInPartyProtocol.IsPlayerInPartyResponse handle(IsPlayerInPartyProtocol.IsPlayerInPartyMessage messageObject, RedisMessageContext context) {
        UUID playerUUID = messageObject.playerUUID();

        boolean isInParty = PartyCache.isInParty(playerUUID);

        return new IsPlayerInPartyProtocol.IsPlayerInPartyResponse(isInParty, true, null);
    }
}