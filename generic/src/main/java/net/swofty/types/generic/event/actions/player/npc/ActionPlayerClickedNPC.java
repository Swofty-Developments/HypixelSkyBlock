package net.swofty.types.generic.event.actions.player.npc;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.entity.npc.NPCEntityImpl;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionPlayerClickedNPC extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerEntityInteractEvent playerEvent = (PlayerEntityInteractEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerEvent.getPlayer();

        if (playerEvent.getHand() != Player.Hand.MAIN) return;

        if (playerEvent.getTarget() instanceof NPCEntityImpl npcImpl) {
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
