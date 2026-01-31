package net.swofty.commons.game;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * @param <G> The game type
 */
public interface GameRegistry<G extends Game<?>> {
	Collection<G> getGames();

	Optional<G> getGameById(String gameId);

	Optional<G> getPlayerGame(UUID playerId);

	/**
	 * Finds a game that can accept a player.
	 *
	 * @return A game with available slots, or empty if none available
	 */
	Optional<G> findAvailableGame();

	G createGame();

	void removeGame(G game);

	void removeGame(String gameId);
}
