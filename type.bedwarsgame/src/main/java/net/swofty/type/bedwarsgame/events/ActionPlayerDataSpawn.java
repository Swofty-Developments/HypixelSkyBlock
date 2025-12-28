package net.swofty.type.bedwarsgame.events;

import net.minestom.server.entity.EntityType;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.pvp.projectile.BowModule;
import net.swofty.pvp.projectile.entities.ArrowProjectile;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerDataSpawn implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
	public void run(PlayerSpawnEvent event) {
		if (!event.isFirstSpawn()) return;
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		BedWarsDataHandler handler = BedWarsDataHandler.getUser(player.getUuid());
		handler.runOnLoad(player);

		// Registers the bow module, which creates event nodes for the player for bow behavior
		new BowModule(player.eventNode(), (p, i) -> new ArrowProjectile(EntityType.ARROW, p));
	}
}