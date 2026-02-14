package net.swofty.type.game.game.event;

public record GameWinConditionEvent(
	String gameId
) implements GameEvent {
	@Override
	public String getGameId() {
		return gameId;
	}
}
