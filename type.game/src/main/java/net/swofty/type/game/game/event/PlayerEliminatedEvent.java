package net.swofty.type.game.game.event;

import net.swofty.type.game.game.Game;

import java.util.UUID;

/**
 * Event fired when a player is eliminated from the game.
 * This is different from leaving - the player may stay as spectator.
 */
public record PlayerEliminatedEvent(
        Game<?> gameId,
        UUID playerId,
        String playerName,
        UUID eliminatorId,
        String eliminatorName,
        EliminationCause cause
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return gameId;
    }

    /**
     * Creates an elimination event without a specific eliminator (e.g., void death).
     */
    public static PlayerEliminatedEvent withoutEliminator(Game<?> game, UUID playerId, String playerName, EliminationCause cause) {
        return new PlayerEliminatedEvent(game, playerId, playerName, null, null, cause);
    }

    public boolean hasEliminator() {
        return eliminatorId != null;
    }

    public enum EliminationCause {
        KILLED,
        VOID,
        ENVIRONMENT,
        DISCONNECTED,
        OTHER
    }
}
