package net.swofty.pvp.events;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player charges a respawn anchor.
 */
public class AnchorChargeEvent implements PlayerInstanceEvent, CancellableEvent {
	
	private final Player player;
	private final Point blockPosition;
	
	private boolean cancelled;
	
	public AnchorChargeEvent(@NotNull Player player, @NotNull Point blockPosition) {
		this.player = player;
		this.blockPosition = blockPosition;
	}
	
	@Override
	public @NotNull Player getPlayer() {
		return player;
	}
	
	public @NotNull Point getBlockPosition() {
		return blockPosition;
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
