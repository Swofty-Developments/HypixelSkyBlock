package net.swofty.type.bedwarsgame.events.custom;

import net.swofty.type.game.game.Game;
import net.swofty.type.game.game.event.GameEvent;

public record BedWarsGameEventAdvanceEvent(
        Game<?> game,
    String previousEvent,
    String currentEvent,
    long secondsUntilNext
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
