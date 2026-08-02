package net.swofty.type.game.game.event;

import net.swofty.type.game.game.Game;

public record CountdownCancelledEvent(
        Game<?> game,
	String reason
) implements GameEvent {
	@Override
    public Game<?> getGame() {
        return game;
	}
}
