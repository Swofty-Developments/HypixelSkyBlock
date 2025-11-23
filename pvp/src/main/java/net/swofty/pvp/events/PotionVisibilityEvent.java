package net.swofty.pvp.events;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Called when an entities potion state (ambient, particle color and invisibility) is updated.
 */
public class PotionVisibilityEvent implements EntityInstanceEvent, CancellableEvent {
	
	private final LivingEntity entity;
	private boolean ambient;
	private List<Particle> particles;
	private boolean invisible;
	
	private boolean cancelled;
	
	public PotionVisibilityEvent(@NotNull LivingEntity entity, boolean ambient,
	                             List<Particle> particles, boolean invisible) {
		this.entity = entity;
		this.ambient = ambient;
		this.particles = particles;
		this.invisible = invisible;
	}
	
	@Override
	public @NotNull LivingEntity getEntity() {
		return entity;
	}
	
	/**
	 * Gets whether the entity effects contain ambient effects.
	 *
	 * @return whether the effects contain ambient effects
	 */
	public boolean isAmbient() {
		return ambient;
	}
	
	/**
	 * Sets whether the entity effects contain ambient effects.
	 *
	 * @param ambient whether the effects contain ambient effects
	 */
	public void setAmbient(boolean ambient) {
		this.ambient = ambient;
	}
	
	/**
	 * Gets the potion particle emitters.
	 * Will be empty for no potion particles.
	 *
	 * @return the potion color
	 */
	public @NotNull List<Particle> getParticles() {
		return particles;
	}
	
	/**
	 * Sets the potion particle emitters.
	 * Set to an empty list to disable potion particles.
	 *
	 * @param particles the potion color
	 */
	public void setParticles(@NotNull List<Particle> particles) {
		this.particles = particles;
	}
	
	/**
	 * Gets whether the entity will become invisible.
	 *
	 * @return whether the entity will become invisible
	 */
	public boolean isInvisible() {
		return invisible;
	}
	
	/**
	 * Sets whether the entity will become invisible.
	 *
	 * @param invisible whether the entity will become invisible
	 */
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
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
