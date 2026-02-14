package net.swofty.type.game.game.event;

public record GameStartEvent(String gameId) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
