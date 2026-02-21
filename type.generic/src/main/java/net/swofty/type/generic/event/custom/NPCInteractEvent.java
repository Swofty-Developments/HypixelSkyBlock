package net.swofty.type.generic.event.custom;

import lombok.Getter;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;


public class NPCInteractEvent implements PlayerInstanceEvent, CancellableEvent {
	private Boolean cancelled = false;
	@Getter
	private final PlayerHand hand;
	@NotNull
	private final HypixelPlayer player;
	@Getter
	private final HypixelNPC npc;

	public NPCInteractEvent(@NotNull HypixelPlayer player, PlayerHand hand, HypixelNPC npc) {
		this.player = player;
		this.hand = hand;
		this.npc = npc;
	}

	@Override
	public @NotNull HypixelPlayer getPlayer() {
		return player;
	}

	public @NotNull HypixelPlayer player() {
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
