package net.swofty.commons.game.respawn;

import java.util.UUID;

/**
 * Interface for handling player respawns in games.
 *
 * @param <P> The player type
 */
public interface RespawnHandler<P> {
	/**
	 * Determines if a player can respawn.
	 *
	 * @param playerId The player's UUID
	 * @return true if the player can respawn
	 */
	boolean canRespawn(UUID playerId);

	/**
	 * Gets the respawn delay for a player.
	 *
	 * @param playerId The player's UUID
	 * @return Respawn delay in seconds
	 */
	int getRespawnDelay(UUID playerId);

	/**
	 * Starts the respawn process for a player.
	 *
	 * @param player The player to respawn
	 */
	void startRespawn(P player);

	/**
	 * Cancels a pending respawn.
	 *
	 * @param playerId The player's UUID
	 */
	void cancelRespawn(UUID playerId);

	/**
	 * Checks if a player is currently in respawn process.
	 */
	boolean isRespawning(UUID playerId);
}
