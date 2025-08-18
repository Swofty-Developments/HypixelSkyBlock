package net.swofty.pvp.events;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player places an end crystal.
 */
public class CrystalPlaceEvent implements PlayerInstanceEvent, CancellableEvent {
	
	private final Player player;
	private Point spawnPosition;
	
	private boolean cancelled;
	
	public CrystalPlaceEvent(@NotNull Player player, @NotNull Point spawnPosition) {
		this.player = player;
		this.spawnPosition = spawnPosition;
	}
	
	@Override
	public @NotNull Player getPlayer() {
		return player;
	}
	
	public @NotNull Point getSpawnPosition() {
		return spawnPosition;
	}
	
	public void setSpawnPosition(@NotNull Point spawnPosition) {
		this.spawnPosition = spawnPosition;
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
