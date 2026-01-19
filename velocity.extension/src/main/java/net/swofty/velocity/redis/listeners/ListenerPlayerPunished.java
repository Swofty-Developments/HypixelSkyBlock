package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.ProxyServer;
import io.sentry.Sentry;
import net.kyori.adventure.text.Component;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.commons.punishment.PunishmentId;
import net.swofty.commons.punishment.PunishmentReason;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.commons.punishment.PunishmentType;
import net.swofty.commons.punishment.template.BanType;
import net.swofty.commons.punishment.template.MuteType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.tinylog.Logger;

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
                throw new JSONException("Missing reason ban, mute or reason_mute");
            }
        } catch (IllegalArgumentException | JSONException e) {
            Logger.error("Failed to parse punishment reason from message: " + message, e);
            Sentry.captureException(e);
            return null;
        }


        PunishmentType punishmentType = PunishmentType.valueOf(type);
        PunishmentReason finalReason = reason;
        SkyBlockVelocity.getServer().getPlayer(target).ifPresent((player) -> {
            PunishmentRedis.ActivePunishment activePunishment = new PunishmentRedis.ActivePunishment(type, id, finalReason, expiresAt);
            switch (punishmentType) {
                case BAN -> {
                    player.disconnect(PunishmentRedis.parseActivePunishmentBanMessage(activePunishment));
                }
                case MUTE -> {
                    player.sendMessage(PunishmentRedis.parseActivePunishmentMuteMessage(activePunishment));
                }
                case WARNING -> {
                    player.sendMessage(Component.text("§c[WARNING] §7You have received a warning for the following reason: §e" + finalReason));
                }
                default -> {
                }
            }
        });

        return null;
    }
}
