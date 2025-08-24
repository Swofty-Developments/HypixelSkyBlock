package net.swofty.type.generic.event.actions.npc;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.entity.animalnpc.HypixelAnimalNPC;
import net.swofty.type.generic.entity.animalnpc.NPCAnimalEntityImpl;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerClickedAnimalNPC implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (event.getHand() != PlayerHand.MAIN) return;
        if (HypixelConst.isIslandServer()) return;

        if (event.getTarget() instanceof NPCAnimalEntityImpl npcImpl) {
            HypixelAnimalNPC npc = HypixelAnimalNPC.getFromImpl(npcImpl);
            if (npc == null) return;

            npc.onClick(new HypixelAnimalNPC.PlayerClickAnimalNPCEvent(
                    player,
                    npc
            ));
        }
    }
}
