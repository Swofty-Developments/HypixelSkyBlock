package net.swofty.pvp.events;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a spectator tries to spectate an entity by attacking it.
 */
public class PlayerSpectateEvent implements PlayerEvent, EntityInstanceEvent, CancellableEvent {
	
	private final Player player;
	private final Entity target;
	
	private boolean cancelled;
	
	public PlayerSpectateEvent(@NotNull Player player, @NotNull Entity target) {
		this.player = player;
		this.target = target;
	}
	
	@Override
	public @NotNull Player getPlayer() {
		return player;
	}
	
	public Entity getTarget() {
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
