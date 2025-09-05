package net.swofty.pvp.events;

import net.swofty.pvp.feature.explosion.ExplosionFeature;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a tnt gets ignited, either by a player or by a nearby explosion.
 * You can get the cause by using {@link ExplosivePrimeEvent#getCause()}
 */
public class ExplosivePrimeEvent implements InstanceEvent, CancellableEvent {
	
	private final Instance instance;
	private final Point blockPosition;
	private final ExplosionFeature.IgnitionCause cause;
	
	private int fuse;
	private boolean cancelled;
	
	public ExplosivePrimeEvent(@NotNull Instance instance, @NotNull Point blockPosition,
	                           @NotNull ExplosionFeature.IgnitionCause cause, int fuse) {
		this.instance = instance;
		this.blockPosition = blockPosition;
		this.cause = cause;
		this.fuse = fuse;
	}
	
	@Override
	public @NotNull Instance getInstance() {
		return instance;
	}
	
	public @NotNull Point getBlockPosition() {
		return blockPosition;
	}
	
	/**
	 * Gets the cause. Can be {@link ExplosionFeature.IgnitionCause.ByPlayer} or {@link ExplosionFeature.IgnitionCause.Explosion}.
	 *
	 * @return the cause
	 */
	public ExplosionFeature.IgnitionCause getCause() {
		return cause;
	}
	
	/**
	 * Gets the fuse in ticks.
	 *
	 * @return the fuse in ticks
	 */
	public int getFuse() {
		return fuse;
	}
	
	/**
	 * Sets the fuse in ticks.
	 *
	 * @param fuse the fuse in ticks
	 */
	public void setFuse(int fuse) {
		this.fuse = fuse;
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
