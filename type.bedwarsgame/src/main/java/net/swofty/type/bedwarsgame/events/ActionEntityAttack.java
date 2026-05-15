package net.swofty.type.bedwarsgame.events;

import net.minestom.server.entity.Entity;
import net.swofty.pvp.events.PrepareAttackEvent;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class ActionEntityAttack implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
	public void run(PrepareAttackEvent event) {
		if (event.getEntity() instanceof BedWarsPlayer player) {
			Game game = player.getGame();
			if (game == null) {
				event.setCancelled(true);
				return;
			}

			if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
				event.setCancelled(true);
				return;
			}

			for (Entity entity : HypixelNPC.getPerPlayerNPCs().get(player.getUuid()).getEntityImpls().values()) {
				if (event.getTarget() == entity) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}

}
