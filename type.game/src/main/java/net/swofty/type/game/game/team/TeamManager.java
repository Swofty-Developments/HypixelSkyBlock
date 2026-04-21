package net.swofty.type.game.game.team;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for team management in team-based games.
 *
 * @param <T> The team type
 * @param <P> The player type
 */
public interface TeamManager<T extends GameTeam, P> {
	Collection<T> getTeams();

	/**
	 * Gets a team by its identifier.
	 */
	Optional<T> getTeam(String teamId);

	/**
	 * Gets the team a player is on.
	 */
	Optional<T> getPlayerTeam(UUID playerId);

	/**
	 * Removes a player from their team.
	 */
	void removeFromTeam(P player);

	/**
	 * @return Active teams (teams with players)
	 */
	Collection<T> getActiveTeams();

	default int getActiveTeamCount() {
		return getActiveTeams().size();
	}

	/**
	 * @return Viable teams (can still win the game)
	 */
	Collection<T> getViableTeams();
}
