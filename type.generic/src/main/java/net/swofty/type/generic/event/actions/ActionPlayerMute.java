package net.swofty.type.generic.event.actions;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.punishment.GetActivePunishmentProtocolObject;
import net.swofty.commons.punishment.ActivePunishment;
import net.swofty.commons.punishment.PunishmentMessages;
import net.swofty.commons.punishment.PunishmentType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.concurrent.TimeUnit;

public class ActionPlayerMute implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        try {
            Object response = new ProxyService(ServiceType.PUNISHMENT)
                    .handleRequest(new GetActivePunishmentProtocolObject.GetActivePunishmentMessage(
                            player.getUuid(), PunishmentType.MUTE.name()))
                    .orTimeout(2, TimeUnit.SECONDS)
                    .join();

            if (response instanceof GetActivePunishmentProtocolObject.GetActivePunishmentResponse muteResponse && muteResponse.found()) {
                event.setCancelled(true);
                ActivePunishment punishment = new ActivePunishment(
                        muteResponse.type(), muteResponse.banId(), muteResponse.reason(), muteResponse.expiresAt(), muteResponse.tags());
                player.sendMessage(PunishmentMessages.muteMessage(punishment));
            }
        } catch (Exception ignored) {
        }
    }
}
