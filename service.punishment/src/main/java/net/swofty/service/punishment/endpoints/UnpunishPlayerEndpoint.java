package net.swofty.service.punishment.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.punishment.UnpunishPlayerProtocolObject;
import net.swofty.commons.punishment.ActivePunishment;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.Optional;

public class UnpunishPlayerEndpoint implements ServiceEndpoint
        <UnpunishPlayerProtocolObject.UnpunishPlayerMessage,
                UnpunishPlayerProtocolObject.UnpunishPlayerResponse> {

    @Override
    public ProtocolObject<UnpunishPlayerProtocolObject.UnpunishPlayerMessage, UnpunishPlayerProtocolObject.UnpunishPlayerResponse> associatedProtocolObject() {
        return new UnpunishPlayerProtocolObject();
    }

    @Override
    public UnpunishPlayerProtocolObject.UnpunishPlayerResponse onMessage(ServiceProxyRequest message, UnpunishPlayerProtocolObject.UnpunishPlayerMessage messageObject) {
        Optional<ActivePunishment> existing = PunishmentRedis.getActive(messageObject.target(), messageObject.type());
        if (existing.isEmpty()) {
            return new UnpunishPlayerProtocolObject.UnpunishPlayerResponse(false, "No active " + messageObject.type().toLowerCase() + " found for this player.");
        }

        try {
            PunishmentRedis.revoke(messageObject.target(), messageObject.type());
        } catch (Exception e) {
            Logger.error("Failed to revoke punishment", e);
            return new UnpunishPlayerProtocolObject.UnpunishPlayerResponse(false, "Failed to revoke punishment from database.");
        }

        Logger.info("Revoked {} for {} by staff {}",
                messageObject.type(), messageObject.target(), messageObject.staff());
        return new UnpunishPlayerProtocolObject.UnpunishPlayerResponse(true, null);
    }
}
