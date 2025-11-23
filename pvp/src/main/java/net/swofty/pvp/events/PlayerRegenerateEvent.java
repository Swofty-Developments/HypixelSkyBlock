package net.swofty.pvp.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player naturally regenerates health.
 */
public class PlayerRegenerateEvent implements PlayerEvent, EntityInstanceEvent, CancellableEvent {
	
	private final Player player;
	private float amount;
	private float exhaustion;
	
	private boolean cancelled;
	
	public PlayerRegenerateEvent(@NotNull Player player, float amount, float exhaustion) {
		this.player = player;
		this.amount = amount;
		this.exhaustion = exhaustion;
	}
	
	@Override
	public @NotNull Player getPlayer() {
		return player;
	}
	
	/**
	 * Returns the amount of health the player will regenerate.
	 *
	 * @return the amount of health
	 */
	public float getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount of health the player will regenerate.
	 *
	 * @param amount the amount of health
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	/**
	 * Returns the amount of exhaustion the regeneration will apply.
	 *
	 * @return the amount of exhaustion
	 */
	public float getExhaustion() {
		return exhaustion;
	}
	
	/**
	 * Sets the amount of exhaustion the regeneration will apply.
	 *
	 * @param exhaustion the amount of exhaustion
	 */
	public void setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
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
