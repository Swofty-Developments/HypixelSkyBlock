package net.swofty.service.punishment.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.punishment.UnpunishPlayerProtocol;
import net.swofty.commons.punishment.ActivePunishment;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.Optional;
import net.swofty.commons.redis.RedisMessageContext;

public class UnpunishPlayerEndpoint implements RedisMessageHandler
        <UnpunishPlayerProtocol.UnpunishPlayerMessage,
                UnpunishPlayerProtocol.UnpunishPlayerResponse> {

    @Override
    public RedisProtocol<UnpunishPlayerProtocol.UnpunishPlayerMessage, UnpunishPlayerProtocol.UnpunishPlayerResponse> protocol() {
        return new UnpunishPlayerProtocol();
    }

    @Override
    public UnpunishPlayerProtocol.UnpunishPlayerResponse handle(UnpunishPlayerProtocol.UnpunishPlayerMessage messageObject, RedisMessageContext context) {
        Optional<ActivePunishment> existing = PunishmentRedis.getActive(messageObject.target(), messageObject.type());
        if (existing.isEmpty()) {
            return new UnpunishPlayerProtocol.UnpunishPlayerResponse(false, "No active " + messageObject.type().toLowerCase() + " found for this player.");
        }

        try {
            PunishmentRedis.revoke(messageObject.target(), messageObject.type());
        } catch (Exception e) {
            Logger.error("Failed to revoke punishment", e);
            return new UnpunishPlayerProtocol.UnpunishPlayerResponse(false, "Failed to revoke punishment from database.");
        }

        Logger.info("Revoked {} for {} by staff {}",
                messageObject.type(), messageObject.target(), messageObject.staff());
        return new UnpunishPlayerProtocol.UnpunishPlayerResponse(true, null);
    }
}
