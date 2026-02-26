package net.swofty.velocity.redis.listeners;

import com.google.gson.Gson;
import io.sentry.Sentry;
import net.kyori.adventure.text.Component;
import net.swofty.commons.proxy.ToProxyChannels;
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
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.List;
import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.PUNISH_PLAYER)
public class ListenerPlayerPunished extends RedisListener {

    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        UUID target = UUID.fromString(message.getString("target"));
        String type = message.getString("type");
        String id = message.getString("id");
        long expiresAt = message.getLong("expiresAt");

        PunishmentReason reason;
        try {
            String banString = message.optString("reason_ban", null);
            String muteString = message.optString("reason_mute", null);

            if (banString != null) {
                reason = new PunishmentReason(BanType.valueOf(banString));
            } else if (muteString != null) {
                reason = new PunishmentReason(MuteType.valueOf(muteString));
            } else {
                throw new org.json.JSONException("Missing reason ban or reason_mute");
            }
        } catch (IllegalArgumentException | org.json.JSONException e) {
            Logger.error("Failed to parse punishment reason from message: " + message, e);
            Sentry.captureException(e);
            return null;
        }

        List<PunishmentTag> tags = List.of();
        if (!message.isNull("tags")) {
            try {
                tags = List.of(new Gson().fromJson(message.getString("tags"), PunishmentTag[].class));
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

        return null;
    }
}
