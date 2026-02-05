package net.swofty.type.game.game.event;

import java.util.UUID;

public record PlayerRespawnCompleteEvent(
        String gameId,
        UUID playerId,
        String playerName
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
