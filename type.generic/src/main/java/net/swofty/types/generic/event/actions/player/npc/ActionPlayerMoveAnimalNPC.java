package net.swofty.types.generic.event.actions.player.npc;

import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerMoveAnimalNPC implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (SkyBlockConst.isIslandServer()) return;

        SkyBlockAnimalNPC.updateForPlayer(player);
    }
}
