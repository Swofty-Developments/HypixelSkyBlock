package net.swofty.types.generic.event.actions.player.npc;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.entity.villager.VillagerEntityImpl;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.VillagerSpokenToEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerClickedVillagerNPC implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run( PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getHand() != Player.Hand.MAIN) return;
        if (SkyBlockConst.isIslandServer()) return;

        if (event.getTarget() instanceof VillagerEntityImpl npcImpl) {
            SkyBlockVillagerNPC npc = SkyBlockVillagerNPC.getFromImpl(npcImpl);
            if (npc == null) return;

            VillagerSpokenToEvent spokenToEvent = new VillagerSpokenToEvent(player, npc);
            SkyBlockEventHandler.callSkyBlockEvent(spokenToEvent);

            if (spokenToEvent.isCancelled()) return;

            npc.onClick(new SkyBlockVillagerNPC.PlayerClickVillagerNPCEvent(
                    player,
                    npc
            ));
        }
    }
}
