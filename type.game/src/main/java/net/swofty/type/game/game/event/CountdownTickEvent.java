package net.swofty.type.game.game.event;

public record CountdownTickEvent(
        String gameId,
        int remainingSeconds,
        boolean shouldAnnounce
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }

    public boolean isComplete() {
        return remainingSeconds <= 0;
    }
}
