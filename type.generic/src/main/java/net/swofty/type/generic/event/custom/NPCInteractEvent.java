package net.swofty.type.generic.event.custom;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jspecify.annotations.NonNull;


public class NPCInteractEvent implements PlayerInstanceEvent, CancellableEvent {
	private Boolean cancelled = false;
	private final PlayerHand hand;
	private final HypixelPlayer player;
	@Getter
	private final HypixelNPC npc;

	public NPCInteractEvent(HypixelPlayer player, PlayerHand hand, HypixelNPC npc) {
		this.player = player;
		this.hand = hand;
		this.npc = npc;
	}

	@Override
	public @NonNull Player getPlayer() {
		return player;
	}

	public HypixelPlayer player() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
