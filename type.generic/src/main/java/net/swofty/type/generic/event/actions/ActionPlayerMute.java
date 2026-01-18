package net.swofty.type.generic.event.actions;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.commons.punishment.PunishmentType;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.Optional;

public class ActionPlayerMute implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        Optional<PunishmentRedis.ActivePunishment> activePunishment = PunishmentRedis.getActive(player.getUuid());
        activePunishment.ifPresent(punishment -> {
            PunishmentType type = PunishmentType.valueOf(punishment.type());
            if (type != PunishmentType.MUTE) {
                return;
            }
            event.setCancelled(true);
            player.sendMessage(PunishmentRedis.parseActivePunishmentMuteMessage(punishment));
        });
    }

}
