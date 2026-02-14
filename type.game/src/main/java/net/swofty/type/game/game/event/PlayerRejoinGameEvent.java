package net.swofty.type.game.game.event;

import net.minestom.server.entity.Player;
import net.swofty.type.game.game.AbstractGame;

public record PlayerRejoinGameEvent(
        String gameId,
        Player player,
        AbstractGame.DisconnectedPlayerData disconnectedPlayerData
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
