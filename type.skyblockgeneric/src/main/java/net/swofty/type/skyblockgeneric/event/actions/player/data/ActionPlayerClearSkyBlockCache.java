package net.swofty.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.packet.packets.client.anticheat.PacketListenerAirJump;
import net.swofty.type.skyblockgeneric.event.actions.player.ActionPlayerStrayTooFar;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import net.swofty.type.skyblockgeneric.server.eventcaller.CustomEventCaller;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockScoreboard;

public class ActionPlayerClearSkyBlockCache implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        // remove from caches
        SkyBlockScoreboard.removeCache(player);
        player.getPetData().updatePetEntityImpl(null);
        PlayerItemOrigin.clearCache(player.getUuid());
        PacketListenerAirJump.playerData.remove(player);
        ActionPlayerStrayTooFar.startedStray.remove(player.getUuid());
        CustomEventCaller.clearCache(player);
    }
}