package net.swofty.commons.game.event;

public record GameStartEvent(String gameId) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
