package net.swofty.type.generic.event.actions.npc;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.NPCEntityImpl;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerClickedNPC implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(PlayerEntityInteractEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		if (event.getHand() != PlayerHand.MAIN) return;

		if (event.getTarget() instanceof NPCEntityImpl npcImpl) {
			HypixelNPC npc = HypixelNPC.getFromImpl(player, npcImpl);
			if (npc == null) return;

			npc.onClick(new HypixelNPC.PlayerClickNPCEvent(
					player,
					npcImpl.getEntityId(),
					npc
			));
		}
	}
}
