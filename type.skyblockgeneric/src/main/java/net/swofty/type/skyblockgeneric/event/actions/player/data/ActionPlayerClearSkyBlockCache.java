package net.swofty.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.event.actions.player.ActionPlayerStrayTooFar;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import net.swofty.type.skyblockgeneric.server.eventcaller.CustomEventCaller;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockScoreboard;

public class ActionPlayerClearSkyBlockCache implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT, order = 10)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        // remove from caches
        SkyBlockScoreboard.removeCache(player);
        player.getPetData().updatePetEntityImpl(null);
        PlayerItemOrigin.clearCache(player.getUuid());
        ActionPlayerStrayTooFar.startedStray.remove(player.getUuid());
        CustomEventCaller.clearCache(player);
    }
}
