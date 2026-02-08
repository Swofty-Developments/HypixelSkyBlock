package net.swofty.pvp.events;

import lombok.Getter;
import lombok.Setter;
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

	@Setter
    @Getter
    private boolean sprint;
	@Setter
    @Getter
    private boolean critical;
	@Getter
    private boolean sweeping;
    @Getter
    private float baseDamage;
    @Setter
    @Getter
    private float enchantsExtraDamage;
    @Setter
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
	 * Gets whether the 1.9+ attack sounds should be played.
	 *
	 * @return whether the attack sounds should be played
	 */
	public boolean hasAttackSounds() {
		return attackSounds;
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

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
