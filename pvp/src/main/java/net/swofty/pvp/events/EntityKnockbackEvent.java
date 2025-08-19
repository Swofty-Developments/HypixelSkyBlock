package net.swofty.pvp.events;

import net.swofty.pvp.feature.knockback.KnockbackSettings;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity gets knocked back by another entity.
 * This event does not apply simply when {@code Entity.takeKnockback()} is called,
 * but only when an entity is attacked by another entity which causes the knockback.
 * <br><br>
 * You should be aware that when the attacker has a knockback weapon, this event will be called twice:
 * once for the default damage knockback, once for the extra knockback.
 * <br>
 * When the attack was a sweeping attack, this event is also called twice for the affected entities:
 * once for the extra sweeping knockback, once for the default knockback.
 * <br><br>
 * You can determine which type of knockback this is by using {@link #getKnockbackType()} ()}.
 */
public class EntityKnockbackEvent implements EntityInstanceEvent, CancellableEvent {
	
	private final Entity entity;
	private final Entity attacker;
    private final KnockbackType knockbackType;
    private AnimationType animationType;
	private KnockbackSettings settings = KnockbackSettings.DEFAULT;
	
	private boolean cancelled;
	
	public EntityKnockbackEvent(@NotNull Entity entity, @NotNull Entity attacker, @NotNull KnockbackType knockbackType, @NotNull AnimationType animationType) {
		this.entity = entity;
		this.attacker = attacker;
        this.knockbackType = knockbackType;
        this.animationType = animationType;
	}
	
	@NotNull
	@Override
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Gets the attacker of the entity. In case of a projectile,
	 * this returns the projectile itself and not the owner.
	 *
	 * @return the attacker
	 */
	@NotNull
	public Entity getAttacker() {
		return attacker;
	}
	
	/**
	 * Gets the type of knockback. See the values of {@link KnockbackType} for more information.
	 *
	 * @return the knockback type
	 */
    public KnockbackType getKnockbackType() {
        return knockbackType;
    }
	
	/**
	 * Gets the settings of the knockback.
	 *
	 * @return the strength
	 */
	public KnockbackSettings getSettings() {
		return settings;
	}
	
	/**
	 * Sets the settings of the knockback.
	 *
	 * @param settings the strength
	 */
	public void setSettings(KnockbackSettings settings) {
		this.settings = settings;
	}

    /**
     * Gets the animation type, which determines how the client tilts the camera on damage.
     *
     * @return the animation type
     * @see AnimationType
     */
    public AnimationType getAnimationType() {
        return animationType;
    }

    /**
     * Sets the animation type, which determines how the client tilts the camera on damage.
     *
     * @param animationType the animation type
     * @see AnimationType
     */
    public void setAnimationType(@NotNull AnimationType animationType) {
        this.animationType = animationType;
    }

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public enum KnockbackType {
		/**
		 * Default knockback from a damage source
		 */
		DAMAGE,
		/**
		 * Attack knockback for strong attacks by players or from the knockback enchantment
		 */
		ATTACK,
		/**
		 * Sweeping knockback when an entity is close to an entity which was hit by a sweeping weapon
		 */
		SWEEPING
	}

    /**
     * @see #DIRECTIONAL
     * @see #FIXED
     */
    public enum AnimationType {
        /**
         * Modern damage animation, which tilts the camera based on the location of the damage source
         */
        DIRECTIONAL,
        /**
         * Legacy damage animation, which always tilts the camera in the same way
         */
        FIXED
    }
}
