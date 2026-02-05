package net.swofty.type.game.game.event;

import java.util.UUID;

/**
 * Event fired when a player disconnects during an active game.
 * Different from PlayerLeaveGameEvent - this tracks disconnection for potential rejoin.
 */
public record PlayerDisconnectGameEvent(
        String gameId,
        UUID playerId,
        String playerName,
        boolean canRejoin
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
