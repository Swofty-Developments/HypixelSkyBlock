package net.swofty.pvp.events;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Called when an explosion will take place. Can be used to modify the affected blocks.
 */
public class ExplosionEvent implements InstanceEvent, CancellableEvent {
	private final Instance instance;
	private final List<Point> affectedBlocks;
	private final List<Entity> affectedEntities;
	
	private Damage damageObject;
	private boolean cancelled;
	
	public ExplosionEvent(@NotNull Instance instance, @NotNull List<Point> affectedBlocks,
	                      @NotNull List<Entity> affectedEntities, @NotNull Damage damageObject) {
		this.instance = instance;
		this.affectedBlocks = affectedBlocks;
		this.affectedEntities = affectedEntities;
		this.damageObject = damageObject;
	}
	
	/**
	 * Gets the blocks affected by this explosion.
	 * The list may be modified.
	 *
	 * @return the list of blocks affected by the explosion
	 */
	public @NotNull List<Point> getAffectedBlocks() {
		return affectedBlocks;
	}
	
	/**
	 * Gets the entities affected by this explosion.
	 * The list may be modified.
	 *
	 * @return the list of entities affected by the explosion
	 */
	public @NotNull List<Entity> getAffectedEntities() {
		return affectedEntities;
	}
	
	/**
	 * Gets the damage object which will be used to damage any affected entities.
	 * The damage amount of this object will be overwritten depending on the entity.
	 *
	 * @return the damage object
	 */
	public @NotNull Damage getDamageObject() {
		return damageObject;
	}
	
	/**
	 * Sets the damage object which will be used to damage any affected entities.
	 * The damage amount of this object will be overwritten depending on the entity.
	 *
	 * @param damageObject the new damage object
	 */
	public void setDamageObject(@NotNull Damage damageObject) {
		this.damageObject = damageObject;
	}
	
	@Override
	public @NotNull Instance getInstance() {
		return instance;
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
