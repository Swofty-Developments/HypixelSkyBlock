package net.swofty.velocity.redis.listeners;

import com.google.gson.Gson;
import io.sentry.Sentry;
import net.kyori.adventure.text.Component;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.to.PunishPlayerProtocol;
import net.swofty.commons.punishment.PunishmentReason;
import net.swofty.commons.punishment.PunishmentTag;
import net.swofty.commons.punishment.ActivePunishment;
import net.swofty.commons.punishment.PunishmentMessages;
import net.swofty.commons.punishment.PunishmentType;
import net.swofty.commons.punishment.template.BanType;
import net.swofty.commons.punishment.template.MuteType;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.tinylog.Logger;

import java.util.List;
import java.util.UUID;

@ChannelListener
public class ListenerPlayerPunished extends RedisListener<
        PunishPlayerProtocol.Request,
        PunishPlayerProtocol.Response> {

    @Override
    public ProtocolObject<PunishPlayerProtocol.Request, PunishPlayerProtocol.Response> getProtocol() {
        return new PunishPlayerProtocol();
    }

    @Override
    public PunishPlayerProtocol.Response receivedMessage(PunishPlayerProtocol.Request message, UUID serverUUID) {
        UUID target = UUID.fromString(message.target());
        String type = message.type();
        String id = message.id();
        long expiresAt = message.expiresAt();

        PunishmentReason reason;
        try {
            String banString = message.reasonBan();
            String muteString = message.reasonMute();

            if (banString != null) {
                reason = new PunishmentReason(BanType.valueOf(banString));
            } else if (muteString != null) {
                reason = new PunishmentReason(MuteType.valueOf(muteString));
            } else {
                throw new IllegalArgumentException("Missing reason ban or reason_mute");
            }
        } catch (IllegalArgumentException e) {
            Logger.error("Failed to parse punishment reason from message: " + message, e);
            Sentry.captureException(e);
            return new PunishPlayerProtocol.Response();
        }

        List<PunishmentTag> tags = List.of();
        if (message.tags() != null) {
            try {
                tags = List.of(new Gson().fromJson(message.tags(), PunishmentTag[].class));
            } catch (Exception ignored) {
            }
        }

        PunishmentType punishmentType = PunishmentType.valueOf(type);
        PunishmentReason finalReason = reason;
        List<PunishmentTag> finalTags = tags;
        SkyBlockVelocity.getServer().getPlayer(target).ifPresent((player) -> {
            ActivePunishment activePunishment = new ActivePunishment(type, id, finalReason, expiresAt, finalTags);
            switch (punishmentType) {
                case BAN -> player.disconnect(PunishmentMessages.banMessage(activePunishment));
                case MUTE -> player.sendMessage(PunishmentMessages.muteMessage(activePunishment));
                case WARNING -> player.sendMessage(Component.text(
                        "§c[WARNING] §7You have received a warning for the following reason: §e" + finalReason.getReasonString()));
                default -> {}
            }
        });

        return new PunishPlayerProtocol.Response();
    }
}
