package net.swofty.type.game.game;

public interface GameCountdownController {
	boolean start();

	void stop();

	default boolean pause() {
		return false;
	}

	default boolean resume() {
		return false;
	}

	boolean isActive();

	default boolean isPaused() {
		return false;
	}

	int getRemainingSeconds();

	void setRemainingSeconds(int seconds);

	void accelerate(int newDuration);
}
