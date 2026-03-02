package net.swofty.type.game.game.event;

import net.minestom.server.entity.Player;

public record PlayerPostJoinGameEvent(
        String gameId,
        Player player,
        int currentPlayerCount,
        int maxPlayerCount
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
