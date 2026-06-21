package net.swofty.type.game.game.event;

public record CountdownCancelledEvent(
	String gameId,
	String reason
) implements GameEvent {
	@Override
	public String getGameId() {
		return gameId;
	}
}
