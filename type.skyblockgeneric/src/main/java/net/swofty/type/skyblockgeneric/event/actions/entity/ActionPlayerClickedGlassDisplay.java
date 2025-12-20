package net.swofty.type.skyblockgeneric.event.actions.entity;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickedGlassDisplay implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(PlayerEntityInteractEvent event) {
		final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

		if (event.getHand() != PlayerHand.MAIN) return;
		if (event.getTarget() instanceof InteractionEntity interaction) {
			interaction.onClick.accept(player, event);
		}
	}
}
