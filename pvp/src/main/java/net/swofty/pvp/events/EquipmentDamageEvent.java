package net.swofty.pvp.events;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an item in an equipment slot gets damaged.
 */
public class EquipmentDamageEvent implements EntityInstanceEvent, CancellableEvent {
	
	private final LivingEntity entity;
	private final EquipmentSlot slot;
	private int amount;
	
	private boolean cancelled;
	
	public EquipmentDamageEvent(@NotNull LivingEntity entity, @NotNull EquipmentSlot slot, int amount) {
		this.entity = entity;
		this.slot = slot;
		this.amount = amount;
	}
	
	@Override
	public @NotNull LivingEntity getEntity() {
		return entity;
	}
	
	/**
	 * Gets the equipment slot in which the item to be damaged is.
	 *
	 * @return the equipment slot
	 */
	public EquipmentSlot getSlot() {
		return slot;
	}
	
	/**
	 * Gets the amount of damage done to the item.
	 *
	 * @return the amount of damage
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount of damage done to the item.
	 *
	 * @param amount the amount of damage
	 */
	public void setAmount(int amount) {
		this.amount = amount;
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
