package net.swofty.service.punishment.endpoints;

import com.google.gson.Gson;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.punishment.PunishPlayerProtocolObject;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.commons.punishment.*;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.punishment.ProxyRedis;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.time.Instant;
import java.util.Optional;

public class PunishPlayerEndpoint implements ServiceEndpoint
        <PunishPlayerProtocolObject.PunishPlayerMessage,
                PunishPlayerProtocolObject.PunishPlayerResponse> {

    @Override
    public ProtocolObject<PunishPlayerProtocolObject.PunishPlayerMessage, PunishPlayerProtocolObject.PunishPlayerResponse> associatedProtocolObject() {
        return new PunishPlayerProtocolObject();
    }

    @Override
    public PunishPlayerProtocolObject.PunishPlayerResponse onMessage(ServiceProxyRequest message, PunishPlayerProtocolObject.PunishPlayerMessage messageObject) {
        PunishmentType punishmentType;
        try {
            punishmentType = PunishmentType.valueOf(messageObject.type());
        } catch (IllegalArgumentException e) {
            return new PunishPlayerProtocolObject.PunishPlayerResponse(false, null, PunishPlayerProtocolObject.ErrorCode.INVALID_TYPE, "The punishment type provided is invalid.");
        }

        Instant now = Instant.now();
        if (messageObject.expiresAt() > 0 && Instant.ofEpochMilli(messageObject.expiresAt()).isBefore(now)) {
            return new PunishPlayerProtocolObject.PunishPlayerResponse(false, null, PunishPlayerProtocolObject.ErrorCode.INVALID_EXPIRY, "The expiration time provided is invalid.");
        }

        boolean hasOverwriteTag = messageObject.tags() != null && messageObject.tags().contains(PunishmentTag.OVERWRITE);
        if (!hasOverwriteTag) {
            Optional<ActivePunishment> existing = PunishmentRedis.getActive(messageObject.target(), messageObject.type());
            if (existing.isPresent()) {
                return new PunishPlayerProtocolObject.PunishPlayerResponse(false, null,
                        PunishPlayerProtocolObject.ErrorCode.ALREADY_PUNISHED, existing.get().banId());
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
            return new PunishPlayerProtocolObject.PunishPlayerResponse(false, null,
                    PunishPlayerProtocolObject.ErrorCode.DATABASE_ERROR, "Failed to save punishment.");
        }

        Gson gson = new Gson();
        ProxyRedis.publishToProxy(ToProxyChannels.PUNISH_PLAYER, new JSONObject()
                .put("target", messageObject.target())
                .put("type", messageObject.type())
                .put("id", id.id())
                .put("reason_ban", reason.getBanType() != null ? reason.getBanType().name() : null)
                .put("reason_mute", reason.getMuteType() != null ? reason.getMuteType().name() : null)
                .put("staff", messageObject.staff())
                .put("issuedAt", now.toEpochMilli())
                .put("expiresAt", messageObject.expiresAt())
                .put("tags", messageObject.tags() != null ? gson.toJson(messageObject.tags()) : null)
        );
        Logger.info("Issued {} punishment to {} for reason '{}' (expires at: {})",
                messageObject.type(),
                messageObject.target(),
                reason.getReasonString(),
                messageObject.expiresAt()
        );
        return new PunishPlayerProtocolObject.PunishPlayerResponse(true, id.id(), null, null);
    }
}
