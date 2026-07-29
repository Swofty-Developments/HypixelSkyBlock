package net.swofty.type.game.game.event;

public record GameDisposeEvent(String gameId) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
