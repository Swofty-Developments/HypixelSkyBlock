package net.swofty.commons.game.event;

import java.util.UUID;

public record PlayerJoinedGameEvent(
        String gameId,
        UUID playerId,
        String playerName,
        int currentPlayerCount,
        int maxPlayerCount
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
