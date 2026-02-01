package net.swofty.type.bedwarsgame.events.custom;

import net.swofty.commons.game.event.GameEvent;

public record BedWarsGameEventAdvanceEvent(
    String gameId,
    String previousEvent,
    String currentEvent,
    long secondsUntilNext
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
