package net.swofty.pvp.events;

import net.swofty.pvp.entity.projectile.AbstractArrow;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player picks up an entity (arrow or trident).
 */
public class PickupEntityEvent implements EntityInstanceEvent, CancellableEvent {
	
	private final Player player;
	private final AbstractArrow arrowEntity;
	
	private boolean cancelled;
	
	public PickupEntityEvent(@NotNull Player player, @NotNull AbstractArrow arrowEntity) {
		this.player = player;
		this.arrowEntity = arrowEntity;
	}
	
	@NotNull
	public Player getPlayer() {
		return player;
	}
	
	@NotNull
	public AbstractArrow getPickedUp() {
		return arrowEntity;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	@Override
	public @NotNull Entity getEntity() {
		return player;
	}
}
