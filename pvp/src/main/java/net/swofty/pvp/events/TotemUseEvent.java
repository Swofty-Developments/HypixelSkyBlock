package net.swofty.pvp.events;

import org.jetbrains.annotations.NotNull;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;

/**
 * Called when a totem prevents an entity from dying.
 */
public class TotemUseEvent implements EntityInstanceEvent, CancellableEvent {
	
	private final LivingEntity entity;
	private final PlayerHand hand;
	
	private boolean cancelled;
	
	public TotemUseEvent(@NotNull LivingEntity entity, @NotNull PlayerHand hand) {
		this.entity = entity;
		this.hand = hand;
	}
	
	@NotNull
	@Override
	public LivingEntity getEntity() {
		return entity;
	}
	
	@NotNull
	public PlayerHand getHand() {
		return hand;
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
