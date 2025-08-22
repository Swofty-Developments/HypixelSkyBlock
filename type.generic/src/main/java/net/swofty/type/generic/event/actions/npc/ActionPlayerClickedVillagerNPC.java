package net.swofty.type.generic.event.actions.npc;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.entity.villager.VillagerEntityImpl;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.event.custom.VillagerSpokenToEvent;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerClickedVillagerNPC implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run( PlayerEntityInteractEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (event.getHand() != PlayerHand.MAIN) return;
        if (HypixelConst.isIslandServer()) return;

        if (event.getTarget() instanceof VillagerEntityImpl npcImpl) {
            HypixelVillagerNPC npc = HypixelVillagerNPC.getFromImpl(npcImpl);
            if (npc == null) return;

            VillagerSpokenToEvent spokenToEvent = new VillagerSpokenToEvent(player, npc);
            HypixelEventHandler.callCustomEvent(spokenToEvent);

            if (spokenToEvent.isCancelled()) return;

            npc.onClick(new HypixelVillagerNPC.PlayerClickVillagerNPCEvent(
                    player,
                    npc
            ));
        }
    }
}
