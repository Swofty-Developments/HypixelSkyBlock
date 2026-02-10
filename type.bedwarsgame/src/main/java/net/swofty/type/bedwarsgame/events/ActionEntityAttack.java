package net.swofty.type.bedwarsgame.events;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.swofty.pvp.events.PrepareAttackEvent;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionEntityAttack implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
	public void run(PrepareAttackEvent event) {
		if (event.getEntity() instanceof BedWarsPlayer player) {
			BedWarsGame game = player.getGame();
			if (game == null) {
				event.setCancelled(true);
				return;
			}

			if (game.getState() != GameState.IN_PROGRESS) {
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

	@HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
	public void run(EntityDamageEvent event) {
		if (event.getEntity() instanceof BedWarsPlayer player) {
			BedWarsGame game = player.getGame();
			if (game == null) {
				return;
			}

			if (game.getState() == GameState.IN_PROGRESS) {
				player.updateBelowTag();
			}
		}
	}

}
