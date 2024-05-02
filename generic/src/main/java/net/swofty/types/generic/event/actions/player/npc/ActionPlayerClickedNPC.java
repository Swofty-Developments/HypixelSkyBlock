package net.swofty.types.generic.event.actions.player.npc;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.entity.npc.NPCEntityImpl;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerClickedNPC implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getHand() != Player.Hand.MAIN) return;

        if (event.getTarget() instanceof NPCEntityImpl npcImpl) {
            SkyBlockNPC npc = SkyBlockNPC.getFromImpl(player, npcImpl);
            if (npc == null) return;

            npc.onClick(new SkyBlockNPC.PlayerClickNPCEvent(
                    player,
                    npcImpl.getEntityId(),
                    npc
            ));
        }
    }
}
