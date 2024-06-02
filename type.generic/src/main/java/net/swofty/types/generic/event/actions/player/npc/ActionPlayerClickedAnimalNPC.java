package net.swofty.types.generic.event.actions.player.npc;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.animalnpc.NPCAnimalEntityImpl;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerClickedAnimalNPC implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getHand() != Player.Hand.MAIN) return;
        if (SkyBlockConst.isIslandServer()) return;

        if (event.getTarget() instanceof NPCAnimalEntityImpl npcImpl) {
            SkyBlockAnimalNPC npc = SkyBlockAnimalNPC.getFromImpl(npcImpl);
            if (npc == null) return;

            npc.onClick(new SkyBlockAnimalNPC.PlayerClickAnimalNPCEvent(
                    player,
                    npc
            ));
        }
    }
}
