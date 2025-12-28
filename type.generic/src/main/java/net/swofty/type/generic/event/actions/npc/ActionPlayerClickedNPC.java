package net.swofty.type.generic.event.actions.npc;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.impl.NPCAnimalEntityImpl;
import net.swofty.type.generic.entity.npc.impl.NPCEntityImpl;
import net.swofty.type.generic.entity.npc.impl.NPCVillagerEntityImpl;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

public class ActionPlayerClickedNPC implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(PlayerEntityInteractEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		if (event.getHand() != PlayerHand.MAIN) return;

		Entity entity = event.getTarget();
		HypixelNPC npc = HypixelNPC.getFromImpl(player, entity);
		if (npc == null) return;

		NPCInteractEvent clickEvent = new NPCInteractEvent(player, event.getHand(), npc);
		HypixelEventHandler.callCustomEvent(clickEvent);
		if (clickEvent.isCancelled()) return;

		switch (entity) {
			case NPCEntityImpl _, NPCVillagerEntityImpl _, NPCAnimalEntityImpl _ -> npc.onClick(clickEvent);
			default -> {
				// This is not a NPC we can handle here
				Logger.warn("Player " + player.getUsername() + " clicked on an unknown NPC type: " + entity.getClass().getName());
			}
		}
	}
}
