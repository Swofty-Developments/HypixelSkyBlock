package net.swofty.service.punishment.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.punishment.PunishPlayerServiceProtocol;
import net.swofty.commons.punishment.*;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.punishment.ProxyRedis;
import org.tinylog.Logger;

import java.time.Instant;
import java.util.Optional;
import net.swofty.commons.redis.RedisMessageContext;

public class PunishPlayerEndpoint implements RedisMessageHandler
        <PunishPlayerServiceProtocol.PunishPlayerMessage,
                PunishPlayerServiceProtocol.PunishPlayerResponse> {

    @Override
    public RedisProtocol<PunishPlayerServiceProtocol.PunishPlayerMessage, PunishPlayerServiceProtocol.PunishPlayerResponse> protocol() {
        return new PunishPlayerServiceProtocol();
    }

    @Override
    public PunishPlayerServiceProtocol.PunishPlayerResponse handle(PunishPlayerServiceProtocol.PunishPlayerMessage messageObject, RedisMessageContext context) {
        PunishmentType punishmentType;
        try {
            punishmentType = PunishmentType.valueOf(messageObject.type());
        } catch (IllegalArgumentException e) {
            return new PunishPlayerServiceProtocol.PunishPlayerResponse(false, null, PunishPlayerServiceProtocol.ErrorCode.INVALID_TYPE, "The punishment type provided is invalid.");
        }

        Instant now = Instant.now();
        if (messageObject.expiresAt() > 0 && Instant.ofEpochMilli(messageObject.expiresAt()).isBefore(now)) {
            return new PunishPlayerServiceProtocol.PunishPlayerResponse(false, null, PunishPlayerServiceProtocol.ErrorCode.INVALID_EXPIRY, "The expiration time provided is invalid.");
        }

        boolean hasOverwriteTag = messageObject.tags() != null && messageObject.tags().contains(PunishmentTag.OVERWRITE);
        if (!hasOverwriteTag) {
            Optional<ActivePunishment> existing = PunishmentRedis.getActive(messageObject.target(), messageObject.type());
            if (existing.isPresent()) {
                return new PunishPlayerServiceProtocol.PunishPlayerResponse(false, null,
                        PunishPlayerServiceProtocol.ErrorCode.ALREADY_PUNISHED, existing.get().banId());
            }
        }

        PunishmentReason reason = messageObject.reason();
        PunishmentId id = PunishmentId.generateId();

        try {
            PunishmentRedis.saveActivePunishment(
                    messageObject.target(),
                    messageObject.type(),
                    id.id(),
                    reason,
                    messageObject.expiresAt(),
                    messageObject.tags()
            );
        } catch (Exception e) {
            Logger.error("Failed to save punishment to Redis", e);
            return new PunishPlayerServiceProtocol.PunishPlayerResponse(false, null,
                    PunishPlayerServiceProtocol.ErrorCode.DATABASE_ERROR, "Failed to save punishment.");
        }

        Gson gson = new Gson();
        ProxyRedis.publishToProxy(new net.swofty.commons.protocol.objects.proxy.to.PunishPlayerProtocol(),
                new net.swofty.commons.protocol.objects.proxy.to.PunishPlayerProtocol.Request(
                        messageObject.target().toString(),
                        messageObject.type(),
                        id.id().toString(),
                        messageObject.expiresAt(),
                        reason.getBanType() != null ? reason.getBanType().name() : null,
                        reason.getMuteType() != null ? reason.getMuteType().name() : null,
                        messageObject.tags() != null ? gson.toJson(messageObject.tags()) : null
                ));
        Logger.info("Issued {} punishment to {} for reason '{}' (expires at: {})",
                messageObject.type(),
                messageObject.target(),
                reason.getReasonString(),
                messageObject.expiresAt()
        );
        return new PunishPlayerServiceProtocol.PunishPlayerResponse(true, id.id(), null, null);
    }
}
