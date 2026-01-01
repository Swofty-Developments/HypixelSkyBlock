package net.swofty.type.generic.event.actions;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerClickedInteractionEntity implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(PlayerEntityInteractEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		if (event.getHand() != PlayerHand.MAIN) return;
		if (event.getTarget() instanceof InteractionEntity interaction) {
			interaction.onClick.accept(player, event);
		}
	}
}
