package net.swofty.commons.game.event;

public record TeamEliminatedEvent(
        String gameId,
        String teamId,
        String teamName
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
