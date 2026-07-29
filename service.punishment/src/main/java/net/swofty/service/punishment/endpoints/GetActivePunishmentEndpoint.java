package net.swofty.service.punishment.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.punishment.GetActivePunishmentProtocol;
import net.swofty.commons.punishment.ActivePunishment;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;
import java.util.Optional;
import net.swofty.commons.redis.RedisMessageContext;

public class GetActivePunishmentEndpoint implements RedisMessageHandler
        <GetActivePunishmentProtocol.GetActivePunishmentMessage,
                GetActivePunishmentProtocol.GetActivePunishmentResponse> {

    @Override
    public RedisProtocol<GetActivePunishmentProtocol.GetActivePunishmentMessage, GetActivePunishmentProtocol.GetActivePunishmentResponse> protocol() {
        return new GetActivePunishmentProtocol();
    }

    @Override
    public GetActivePunishmentProtocol.GetActivePunishmentResponse handle(GetActivePunishmentProtocol.GetActivePunishmentMessage messageObject, RedisMessageContext context) {
        Optional<ActivePunishment> existing = PunishmentRedis.getActive(messageObject.target(), messageObject.type());
        if (existing.isEmpty()) {
            return new GetActivePunishmentProtocol.GetActivePunishmentResponse(false, null, null, null, 0, List.of(), true, null);
        }

        ActivePunishment punishment = existing.get();
        return new GetActivePunishmentProtocol.GetActivePunishmentResponse(
                true,
                punishment.type(),
                punishment.banId(),
                punishment.reason(),
                punishment.expiresAt(),
                punishment.tags(),
                true,
                null
        );
    }
}
