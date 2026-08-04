package net.swofty.type.game.game.event;

import net.swofty.type.game.game.Game;

public record CountdownTickEvent(
        Game<?> game,
        int remainingSeconds,
        boolean shouldAnnounce
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }

    public boolean isComplete() {
        return remainingSeconds <= 0;
    }
}
