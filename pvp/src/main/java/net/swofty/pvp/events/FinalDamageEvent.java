package net.swofty.pvp.events;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The damage event containing the final damage, calculation of armor and effects included.
 * This event should be used, unless you want to detect how much damage was originally dealt.
 */
public class FinalDamageEvent implements EntityInstanceEvent, CancellableEvent {
	
	private final LivingEntity entity;
	private final Damage damage;
	private int invulnerabilityTicks;
	private boolean animation;
	
	private boolean cancelled;
	
	public FinalDamageEvent(@NotNull LivingEntity entity, @NotNull Damage damage,
	                        int invulnerabilityTicks, boolean animation) {
		this.entity = entity;
		this.damage = damage;
		this.invulnerabilityTicks = invulnerabilityTicks;
		this.animation = animation;
	}
	
	@NotNull
	@Override
	public LivingEntity getEntity() {
		return entity;
	}
	
	/**
	 * Gets the damage info, which can be used to set the damage amount.
	 *
	 * @return the damage info
	 */
	public Damage getDamage() {
		return damage;
	}
	
	/**
	 * Gets the amount of ticks the entity is invulnerable after the damage has been applied.
	 * By default it is 10 (half a second).
	 *
	 * @return the amount of ticks the entity is invulnerable
	 */
	public int getInvulnerabilityTicks() {
		return invulnerabilityTicks;
	}
	
	/**
	 * Sets the amount of ticks the entity is invulnerable after the damage has been applied.
	 * By default it is 10 (half a second).
	 *
	 * @param invulnerabilityTicks the amount of ticks the entity is invulnerable
	 */
	public void setInvulnerabilityTicks(int invulnerabilityTicks) {
		this.invulnerabilityTicks = invulnerabilityTicks;
	}

	public boolean shouldAnimate() {
		return animation;
	}
	
	public void setAnimate(boolean animate) {
        this.animation = animate;
    }
	/**
	 * Checks if the damage will kill the entity.
	 * <br><br>
	 * This requires some computing, caching the result may
	 * be better if used frequently.
	 *
	 * @return {@code true} if the entity will be killed, {@code false} otherwise
	 */
	public boolean doesKillEntity() {
		float remainingDamage = damage.getAmount();
		
		// Additional hearts support
		if (entity instanceof final Player player) {
			final float additionalHearts = player.getAdditionalHearts();
			if (additionalHearts > 0) {
				if (remainingDamage > additionalHearts) {
					remainingDamage -= additionalHearts;
				} else {
					remainingDamage = 0;
				}
			}
		}
		
		float finalHealth = entity.getHealth() - remainingDamage;
		return finalHealth <= 0;
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
