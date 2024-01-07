package net.swofty.commons.skyblock.event.actions.player.npc;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.commons.skyblock.entity.npc.NPCEntityImpl;
import net.swofty.commons.skyblock.entity.npc.SkyBlockNPC;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Checks to see if a player clicks on an NPC",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = true)
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
            SkyBlockNPC npc = SkyBlockNPC.getFromImpl(npcImpl);
            if (npc == null) return;

            npc.onClick(new SkyBlockNPC.PlayerClickNPCEvent(
                    player,
                    npcImpl.getEntityId(),
                    npc
            ));
        }
    }
}
