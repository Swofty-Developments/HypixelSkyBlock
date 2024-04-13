package net.swofty.types.generic.event.actions.player.npc;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.animalnpc.NPCAnimalEntityImpl;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.types.generic.entity.npc.NPCEntityImpl;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Checks to see if a player clicks on an Animal NPC",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerClickedAnimalNPC extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerEntityInteractEvent event = (PlayerEntityInteractEvent) tempEvent;
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
