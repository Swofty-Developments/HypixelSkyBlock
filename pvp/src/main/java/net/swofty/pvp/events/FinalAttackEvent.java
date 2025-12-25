package net.swofty.pvp.events;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Gets called when a player attacks an entity.
 * This event can be used to set a few variables:<br>
 * - sprint attack<br>
 * - critical attack<br>
 * - sweeping attack<br>
 * - base damage of the attack<br>
 * - enchantment extra damage of the attack<br>
 * - whether the attack sounds should be played<br>
 */
public class FinalAttackEvent implements EntityInstanceEvent, CancellableEvent {
	
	private final Entity entity;
	private final Entity target;
	
	private boolean sprint;
	private boolean critical;
	private boolean sweeping;
	private float baseDamage;
	private float enchantsExtraDamage;
	private boolean attackSounds;
	private boolean playSoundsOnFail;
	
	private boolean cancelled;
	
	public FinalAttackEvent(@NotNull Entity entity, @NotNull Entity target,
	                        boolean sprint, boolean critical, boolean sweeping,
	                        float baseDamage, float enchantsExtraDamage,
	                        boolean attackSounds, boolean playSoundsOnFail) {
		this.entity = entity;
		this.target = target;
		this.sprint = sprint;
		this.critical = critical;
		this.sweeping = sweeping;
		this.baseDamage = baseDamage;
		this.enchantsExtraDamage = enchantsExtraDamage;
		this.attackSounds = attackSounds;
		this.playSoundsOnFail = playSoundsOnFail;
	}
	
	@Override
	public @NotNull Entity getEntity() {
		return entity;
	}
	
	public @NotNull Entity getTarget() {
		return target;
	}
	
	/**
	 * Gets whether the attack was a sprint attack.
	 *
	 * @return whether the attack was a sprint attack or not
	 */
	public boolean isSprint() {
		return sprint;
	}
	
	/**
	 * Sets whether the attack was a sprint attack.
	 *
	 * @param sprint whether the attack was a sprint attack or not
	 */
	public void setSprint(boolean sprint) {
		this.sprint = sprint;
	}
	
	/**
	 * Gets whether the attack was critical.
	 *
	 * @return whether the attack was critical or not
	 */
	public boolean isCritical() {
		return critical;
	}
	
	/**
	 * Sets whether the attack was critical.
	 *
	 * @param crit whether the attack was critical or not
	 */
	public void setCritical(boolean crit) {
		this.critical = crit;
	}
	
	/**
	 * Gets whether the attack was sweeping.
	 *
	 * @return whether the attack as sweeping or not
	 */
	public boolean isSweeping() {
		return sweeping;
	}
	
	/**
	 * Sets whether the attack was sweeping.
	 *
	 * @param sweeping whether the attack was sweeping or not
	 */
	public void setSweeping(boolean sweeping) {
		this.sweeping = sweeping;
	}
	
	/**
	 * Gets the base damage of the attack.
	 * Tool enchantments are excluded, but attack cooldown strength (for 1.9+) is not.
	 *
	 * @return the base damage of the attack
	 */
	public float getBaseDamage() {
		return baseDamage;
	}
	
	/**
	 * Sets the base damage of the attack.
	 * Enchantment extra damage will be added to this to get the final damage.
	 *
	 * @param baseDamage the base damage of the attack
	 */
	public void setBaseDamage(float baseDamage) {
		this.baseDamage = baseDamage;
	}
	
	/**
	 * Gets the extra damage from enchantments of the attack
	 * (e.g. sharpness, but also mob based, e.g. impaling).
	 * This does not include the base damage.
	 *
	 * @return the extra damage from enchantments of the attack
	 */
	public float getEnchantsExtraDamage() {
		return enchantsExtraDamage;
	}
	
	/**
	 * Sets the extra damage of the attack.
	 * This will be added to the base damage to get the final damage.
	 *
	 * @param enchantsExtraDamage the extra damage of the attack
	 */
	public void setEnchantsExtraDamage(float enchantsExtraDamage) {
		this.enchantsExtraDamage = enchantsExtraDamage;
	}
	
	/**
	 * Gets whether the 1.9+ attack sounds should be played.
	 *
	 * @return whether the attack sounds should be played
	 */
	public boolean hasAttackSounds() {
		return attackSounds;
	}
	
	/**
	 * Sets whether the 1.9+ attack sounds should be played.
	 * This also works for legacy.
	 *
	 * @param attackSounds whether the attack sounds should be played
	 */
	public void setAttackSounds(boolean attackSounds) {
		this.attackSounds = attackSounds;
	}
	
	/**
	 * Gets whether the 1.9+ attack sounds should be played if the damage failed.
	 * This only applies if hasAttackSounds() is true.
	 *
	 * If this is true, the only sounds that may be played are knockback and nodamage.
	 *
	 * @return whether the attack sounds should be played if the damage failed
	 */
	public boolean playSoundsOnFail() {
		return playSoundsOnFail;
	}
	
	/**
	 * Sets whether the 1.9+ attack sounds should be played if the damage failed.
	 * This only applies if hasAttackSounds() is true.
	 *
	 * If this is true, the only sounds that may be played are knockback and nodamage.
	 *
	 * @param playSoundsOnFail whether the attack sounds should be played if the damage failed
	 */
	public void setPlaySoundsOnFail(boolean playSoundsOnFail) {
		this.playSoundsOnFail = playSoundsOnFail;
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
