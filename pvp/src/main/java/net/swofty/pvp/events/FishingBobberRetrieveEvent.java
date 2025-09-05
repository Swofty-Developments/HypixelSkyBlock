package net.swofty.pvp.events;

import net.swofty.pvp.entity.projectile.FishingBobber;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The event called whenever a player retrieves a fishing bobber.
 */
public class FishingBobberRetrieveEvent implements PlayerInstanceEvent, CancellableEvent {
	
	private final Player player;
	private final FishingBobber bobber;
	
	private boolean cancelled;
	
	public FishingBobberRetrieveEvent(Player player, FishingBobber bobber) {
		this.player = player;
		this.bobber = bobber;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	@Override
	public @NotNull Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the fishing bobber which was retrieved in the event.
	 *
	 * @return the fishing bobber
	 */
	public FishingBobber getBobber() {
		return bobber;
	}
}
