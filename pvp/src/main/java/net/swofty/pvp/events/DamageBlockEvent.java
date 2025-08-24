package net.swofty.pvp.events;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity blocks damage using a shield.
 * This event can be used to set the resulting damage.
 */
public class DamageBlockEvent implements EntityInstanceEvent, CancellableEvent {
	
	private final LivingEntity entity;
	private final float damage;
	private float resultingDamage;
	private boolean knockbackAttacker;
	
	private boolean cancelled;
	
	public DamageBlockEvent(@NotNull LivingEntity entity, float damage,
	                        float resultingDamage, boolean knockbackAttacker) {
		this.entity = entity;
		this.damage = damage;
		this.resultingDamage = resultingDamage;
		this.knockbackAttacker = knockbackAttacker;
	}
	
	@NotNull
	@Override
	public LivingEntity getEntity() {
		return entity;
	}
	
	public boolean knockbackAttacker() {
		return knockbackAttacker;
	}
	
	/**
	 * This fixes a bug introduced in 1.14. Prior to 1.14, the attacker would receive
	 * knockback when the victim was blocking. In 1.14 and above, this is no longer the case.
	 * To apply the fix, set this to true (true by default).
	 *
	 * @param knockbackAttacker true if the attacker should be knocked back
	 */
	public void setKnockbackAttacker(boolean knockbackAttacker) {
		this.knockbackAttacker = knockbackAttacker;
	}
	
	/**
	 * Gets the original damage dealt.
	 *
	 * @return the original damage
	 */
	public float getDamage() {
		return damage;
	}
	
	public float getResultingDamage() {
		return resultingDamage;
	}
	
	/**
	 * Sets the resulting damage after the block.
	 *
	 * @param resultingDamage the resulting damage
	 */
	public void setResultingDamage(float resultingDamage) {
		this.resultingDamage = resultingDamage;
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
