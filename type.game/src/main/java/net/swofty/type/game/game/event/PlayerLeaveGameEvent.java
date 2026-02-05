package net.swofty.type.game.game.event;

import java.util.UUID;

/**
 * Event fired when a player leaves a game (voluntarily).
 */
public record PlayerLeaveGameEvent(
        String gameId,
        UUID playerId,
        String playerName,
        LeaveReason reason
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }

    public enum LeaveReason {
        /**
         * Player chose to leave voluntarily.
         */
        VOLUNTARY,

        /**
         * Player was kicked from the game.
         */
        KICKED,

        /**
         * Player was eliminated from the game.
         */
        ELIMINATED,

        /**
         * Game ended.
         */
        GAME_END
    }
}
