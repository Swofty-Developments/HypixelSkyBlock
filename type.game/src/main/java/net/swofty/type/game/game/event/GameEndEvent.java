package net.swofty.type.game.game.event;

public record GameEndEvent(
	String gameId,
	GameResult result
) implements GameEvent {
	@Override
	public String getGameId() {
		return gameId;
	}

	public sealed interface GameResult permits GameResult.Victory, GameResult.Draw, GameResult.Cancelled {
		record Victory(String winnerId, String winnerName, WinnerType winnerType) implements GameResult {
			public enum WinnerType {
				PLAYER,
				TEAM
			}
		}

		record Draw(String reason) implements GameResult {
		}

		record Cancelled(String reason) implements GameResult {
		}
	}
}
