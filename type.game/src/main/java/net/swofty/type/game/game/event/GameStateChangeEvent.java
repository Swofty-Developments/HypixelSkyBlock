package net.swofty.type.game.game.event;

import net.swofty.type.game.game.GameState;

/**
 * Event fired when a game's state changes.
 * Listen to this for game lifecycle management.
 */
public record GameStateChangeEvent(
	String gameId,
	GameState previousState,
	GameState newState
) implements GameEvent {
	@Override
	public String getGameId() {
		return gameId;
	}

	/**
	 * @return true if the game just started (transitioned to IN_PROGRESS)
	 */
	public boolean isGameStart() {
		return newState == GameState.IN_PROGRESS && previousState != GameState.IN_PROGRESS;
	}

	/**
	 * @return true if the game just ended (transitioned to ENDING)
	 */
	public boolean isGameEnd() {
		return newState == GameState.ENDING && previousState != GameState.ENDING;
	}

	/**
	 * @return true if countdown just started
	 */
	public boolean isCountdownStart() {
		return newState == GameState.COUNTDOWN && previousState == GameState.WAITING;
	}
}
