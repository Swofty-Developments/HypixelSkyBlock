package net.swofty.type.game.game;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.game.game.event.CountdownCancelledEvent;
import net.swofty.type.game.game.event.CountdownTickEvent;

import java.util.function.Consumer;

public class DefaultGameCountdown implements GameCountdownController {
	private final String gameId;
	private final CountdownConfig config;
	private final Consumer<Object> eventDispatcher;
	private final Runnable onComplete;
	private final CanStartCheck canStartCheck;

	private int remainingSeconds;
	private boolean active = false;
	private boolean paused = false;
	private Task countdownTask;

	@FunctionalInterface
	public interface CanStartCheck {
		boolean canContinue();
	}

	/**
	 * Creates a new countdown controller.
	 *
	 * @param gameId          The game ID for events
	 * @param config          Countdown configuration
	 * @param eventDispatcher Consumer that dispatches events (use HypixelEventHandler::callCustomEvent)
	 * @param onComplete      Called when countdown completes
	 * @param canStartCheck   Check if countdown should continue (e.g., has minimum players)
	 */
	public DefaultGameCountdown(
		String gameId,
		CountdownConfig config,
		Consumer<Object> eventDispatcher,
		Runnable onComplete,
		CanStartCheck canStartCheck
	) {
		this.gameId = gameId;
		this.config = config;
		this.eventDispatcher = eventDispatcher;
		this.onComplete = onComplete;
		this.canStartCheck = canStartCheck;
		this.remainingSeconds = config.durationSeconds();
	}

	@Override
	public boolean start() {
		if (active || !canStartCheck.canContinue()) {
			return false;
		}

		active = true;
		paused = false;
		remainingSeconds = config.durationSeconds();

		// Fire initial tick event
		dispatchTickEvent();

		countdownTask = MinecraftServer.getSchedulerManager()
			.buildTask(this::tick)
			.delay(TaskSchedule.seconds(1))
			.repeat(TaskSchedule.seconds(1))
			.schedule();

		return true;
	}

	@Override
	public void stop() {
		if (!active) return;

		active = false;
		cancelTask();
		eventDispatcher.accept(new CountdownCancelledEvent(gameId, "Countdown stopped"));
	}

	@Override
	public boolean pause() {
		if (!active || paused) return false;
		paused = true;
		return true;
	}

	@Override
	public boolean resume() {
		if (!active || !paused) return false;
		paused = false;
		return true;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isPaused() {
		return paused;
	}

	@Override
	public int getRemainingSeconds() {
		return remainingSeconds;
	}

	@Override
	public void setRemainingSeconds(int seconds) {
		this.remainingSeconds = Math.max(0, seconds);
		if (active) {
			dispatchTickEvent();
		}
	}

	@Override
	public void accelerate(int newDuration) {
		if (remainingSeconds > newDuration) {
			remainingSeconds = newDuration;
			if (active) {
				dispatchTickEvent();
			}
		}
	}

	private void tick() {
		if (!active) {
			cancelTask();
			return;
		}

		if (paused) {
			return;
		}

		if (!canStartCheck.canContinue()) {
			stop();
			eventDispatcher.accept(new CountdownCancelledEvent(gameId, "Not enough players"));
			return;
		}

		remainingSeconds--;
		dispatchTickEvent();

		if (remainingSeconds <= 0) {
			active = false;
			cancelTask();
			onComplete.run();
		}
	}

	private void dispatchTickEvent() {
		eventDispatcher.accept(new CountdownTickEvent(
			gameId,
			remainingSeconds,
			config.shouldAnnounce(remainingSeconds)
		));
	}

	private void cancelTask() {
		if (countdownTask != null) {
			countdownTask.cancel();
			countdownTask = null;
		}
	}

	public void checkConditions() {
		if (active && !canStartCheck.canContinue()) {
			stop();
		}
	}
}
