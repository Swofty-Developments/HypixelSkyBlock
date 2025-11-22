package net.swofty.pvp.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a players' exhaustion level changes.
 * This is used to determine when their food level should change.
 */
public class PlayerExhaustEvent implements PlayerEvent, EntityInstanceEvent, CancellableEvent {
	
	private final Player player;
	private float amount;
	
	private boolean cancelled;
	
	public PlayerExhaustEvent(@NotNull Player player, float amount) {
		this.player = player;
		this.amount = amount;
	}
	
	@Override
	public @NotNull Player getPlayer() {
		return player;
	}
	
	/**
	 * Returns the amount of exhaustion.
	 *
	 * @return the amount of exhaustion
	 */
	public float getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount of exhaustion.
	 * Example: One sprint jump applies 0.8 exhaustion in 1.8, and 0.2 in newer versions.
	 *
	 * @param amount the amount of exhaustion
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
