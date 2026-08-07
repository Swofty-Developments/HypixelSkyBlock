package net.swofty.type.game.game.event;

import net.swofty.type.game.game.Game;

public record GameDisposeEvent(Game<?> game) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
