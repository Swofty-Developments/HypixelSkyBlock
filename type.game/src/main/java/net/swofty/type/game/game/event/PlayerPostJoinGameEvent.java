package net.swofty.type.game.game.event;

import net.minestom.server.entity.Player;
import net.swofty.type.game.game.Game;

public record PlayerPostJoinGameEvent(
        Game<?> game,
        Player player,
        int currentPlayerCount,
        int maxPlayerCount
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
