package net.swofty.type.prototypelobby.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerBlockBreak implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onBlockBreak(PlayerBlockBreakEvent event) {
        if (HypixelConst.getTypeLoader().getType() != ServerType.PROTOTYPE_LOBBY) return;

        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) return;

        event.setCancelled(true);
    }
}
