package net.swofty.type.game.game;

import net.minestom.server.instance.InstanceContainer;
import net.swofty.type.game.game.event.GameTeamWinConditionEvent;
import net.swofty.type.game.game.event.PlayerAssignedTeamEvent;
import net.swofty.type.game.game.event.TeamEliminatedEvent;
import net.swofty.type.game.game.team.GameTeam;
import net.swofty.type.game.game.team.TeamManager;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Abstract base class for team-based games.
 * Extends AbstractGame with team management capabilities.
 *
 * @param <P> The player type
 * @param <T> The team type
 */
public abstract class AbstractTeamGame<P extends GameParticipant, T extends GameTeam>
        extends AbstractGame<P> implements TeamManager<T, P> {

    protected final Map<String, T> teams = new LinkedHashMap<>();
    protected final Map<UUID, String> playerTeams = new HashMap<>();

    protected AbstractTeamGame(InstanceContainer instance, Consumer<Object> eventDispatcher) {
        super(instance, eventDispatcher);
    }

    /**
     * Registers teams for this game.
     * Call this during initialization.
     */
    protected void registerTeam(T team) {
        teams.put(team.getId(), team);
    }

    @Override
    public Collection<T> getTeams() {
        return Collections.unmodifiableCollection(teams.values());
    }

    @Override
    public Optional<T> getTeam(String teamId) {
        return Optional.ofNullable(teams.get(teamId));
    }

    @Override
    public Optional<T> getPlayerTeam(UUID playerId) {
        String teamId = playerTeams.get(playerId);
        return teamId != null ? getTeam(teamId) : Optional.empty();
    }

    @Override
    public void removeFromTeam(P player) {
        String currentTeamId = playerTeams.remove(player.getUuid());
        if (currentTeamId != null) {
            getTeam(currentTeamId).ifPresent(team -> team.removePlayer(player.getUuid()));
        }
    }

    @Override
    public Collection<T> getActiveTeams() {
        return teams.values().stream()
                .filter(GameTeam::hasPlayers)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<T> getViableTeams() {
        return teams.values().stream()
                .filter(this::isTeamViable)
                .collect(Collectors.toList());
    }

    /**
     * Determines if a team can still win the game.
     */
    protected abstract boolean isTeamViable(T team);

    protected void onTeamEliminated(T team) {
        eventDispatcher.accept(new TeamEliminatedEvent(
                gameId,
                team.getId(),
                team.getName()
        ));
    }

    /**
     * Auto-assigns all unassigned players to teams.
     * Override for custom assignment logic.
     */
    public void autoAssignTeams() {
        List<P> unassignedPlayers = players.values().stream()
                .filter(p -> !playerTeams.containsKey(p.getUuid()))
                .collect(Collectors.toList());

        Collections.shuffle(unassignedPlayers);

        List<T> availableTeams = new ArrayList<>(teams.values());
        int teamIndex = 0;
        int teamSize = getTeamSize();

        for (P player : unassignedPlayers) {
            // Find a team that isn't full
            T targetTeam = null;
            for (int i = 0; i < availableTeams.size(); i++) {
                T team = availableTeams.get((teamIndex + i) % availableTeams.size());
                if (team.getPlayerCount() < teamSize) {
                    targetTeam = team;
                    teamIndex = (teamIndex + i + 1) % availableTeams.size();
                    break;
                }
            }

            if (targetTeam != null) {
                // Remove from current team if any
                removeFromTeam(player);

                // Add to new team
                targetTeam.addPlayer(player.getUuid());
                playerTeams.put(player.getUuid(), targetTeam.getId());

                eventDispatcher.accept(new PlayerAssignedTeamEvent<T>(
                    gameId,
                    player.getServerPlayer(),
                    targetTeam
                ));
            }
        }
    }

    /**
     * @return The maximum team size for this game
     */
    protected abstract int getTeamSize();

    @Override
    protected void checkWinConditions() {
        if (state != GameState.IN_PROGRESS) return;

        List<T> viableTeams = getViableTeams().stream().toList();
        Logger.info("Viable teams: {}", viableTeams.stream().map(T::getName).toList());
        if (viableTeams.size() <= 1) {
            T winner = viableTeams.isEmpty() ? null : viableTeams.getFirst();

            Logger.info("Game {} ended. Winner: {}", gameId, winner != null ? winner.getName() : "None");

            eventDispatcher.accept(
                new GameTeamWinConditionEvent<>(
                    gameId,
                    winner
                )
            );
        }
    }
}
