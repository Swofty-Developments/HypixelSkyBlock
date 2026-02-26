package net.swofty.service.punishment.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.punishment.GetActivePunishmentProtocolObject;
import net.swofty.commons.punishment.ActivePunishment;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;
import java.util.Optional;

public class GetActivePunishmentEndpoint implements ServiceEndpoint
        <GetActivePunishmentProtocolObject.GetActivePunishmentMessage,
                GetActivePunishmentProtocolObject.GetActivePunishmentResponse> {

    @Override
    public ProtocolObject<GetActivePunishmentProtocolObject.GetActivePunishmentMessage, GetActivePunishmentProtocolObject.GetActivePunishmentResponse> associatedProtocolObject() {
        return new GetActivePunishmentProtocolObject();
    }

    @Override
    public GetActivePunishmentProtocolObject.GetActivePunishmentResponse onMessage(ServiceProxyRequest message, GetActivePunishmentProtocolObject.GetActivePunishmentMessage messageObject) {
        Optional<ActivePunishment> existing = PunishmentRedis.getActive(messageObject.target(), messageObject.type());
        if (existing.isEmpty()) {
            return new GetActivePunishmentProtocolObject.GetActivePunishmentResponse(false, null, null, null, 0, List.of());
        }

        ActivePunishment punishment = existing.get();
        return new GetActivePunishmentProtocolObject.GetActivePunishmentResponse(
                true,
                punishment.type(),
                punishment.banId(),
                punishment.reason(),
                punishment.expiresAt(),
                punishment.tags()
        );
    }
}
