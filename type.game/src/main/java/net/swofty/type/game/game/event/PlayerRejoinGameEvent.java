package net.swofty.type.game.game.event;

import net.minestom.server.entity.Player;
import net.swofty.type.game.game.AbstractGame;
import net.swofty.type.game.game.Game;

public record PlayerRejoinGameEvent(
        Game<?> gameId,
        Player player,
        AbstractGame.DisconnectedPlayerData disconnectedPlayerData
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return gameId;
    }
}
