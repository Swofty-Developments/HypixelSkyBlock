package net.swofty.pvp.events;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import org.jetbrains.annotations.NotNull;

public class PrepareAttackEvent implements EntityInstanceEvent, CancellableEvent {

	private final Entity entity;
	private final Entity target;

	private boolean cancelled;

	public PrepareAttackEvent(@NotNull Entity entity, @NotNull Entity target) {
		this.entity = entity;
		this.target = target;
	}

	@Override
	public @NotNull Entity getEntity() {
		return entity;
	}

	public @NotNull Entity getTarget() {
		return target;
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
