package net.swofty.type.game.game.event;

import net.minestom.server.entity.Player;

/**
 * Event fired when a player disconnects during an active game.
 * Different from PlayerLeaveGameEvent - this tracks disconnection for potential rejoin.
 */
public record PlayerDisconnectGameEvent(
        String gameId,
        Player player,
        boolean canRejoin
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
