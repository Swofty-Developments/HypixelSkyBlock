package net.swofty.type.game.game;

public enum GameState {
	WAITING,
	COUNTDOWN,
	IN_PROGRESS,
	ENDING,
	TERMINATED;

	public boolean isInProgress() {
		return this == IN_PROGRESS;
	}

	public boolean isWaiting() {
		return this == WAITING || this == COUNTDOWN;
	}

	public boolean isEnding() {
		return this == ENDING || this == TERMINATED;
	}
}
