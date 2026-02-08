package net.swofty.type.game.game.event;

import java.util.UUID;

public record PlayerRespawnStartEvent(
        String gameId,
        UUID playerId,
        String playerName,
        int respawnDelaySeconds
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
