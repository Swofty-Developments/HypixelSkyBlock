package net.swofty.type.generic.event.actions.npc;

import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.entity.animalnpc.HypixelAnimalNPC;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerMoveAnimalNPC implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (HypixelConst.isIslandServer()) return;

        HypixelAnimalNPC.updateForPlayer(player);
    }
}
