package net.swofty.type.bedwarsgame.game;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.tinylog.Logger;

import javax.annotation.Nullable;

public final class GameCountdown {
	private static final int COUNTDOWN_DURATION_SECONDS = 30;
	private static final int[] ANNOUNCEMENT_TIMES = {30, 20, 15, 10, 5, 4, 3, 2, 1};

	private final Game game;
	@Getter
	private int remainingSeconds;
	@Getter
	private boolean isActive;
	@Nullable
	private Task countdownTask;

	public GameCountdown(Game game) {
		this.game = game;
		this.remainingSeconds = COUNTDOWN_DURATION_SECONDS;
		this.isActive = false;
	}

	public boolean startCountdown() {
		if (isActive || game.getGameStatus() != GameStatus.WAITING) {
			return false;
		}

		if (!hasMinimumPlayers()) {
			Logger.debug("Cannot start countdown: insufficient players");
			return false;
		}

		isActive = true;
		remainingSeconds = COUNTDOWN_DURATION_SECONDS;

		Logger.info("Starting countdown for game {}", game.getGameId());
		announceCountdown();

		countdownTask = MinecraftServer.getSchedulerManager()
				.buildTask(this::tickCountdown)
				.delay(TaskSchedule.seconds(1))
				.repeat(TaskSchedule.seconds(1))
				.schedule();

		return true;
	}

	public void stopCountdown() {
		if (!isActive) {
			return;
		}

		Logger.info("Stopping countdown for game {}", game.getGameId());
		isActive = false;

		if (countdownTask != null) {
			countdownTask.cancel();
			countdownTask = null;
		}

		game.getPlayers().forEach(player ->
				player.sendMessage(Component.text("Countdown stopped - not enough players!")
						.color(NamedTextColor.RED))
		);
	}

	public void checkCountdownConditions() {
		if (!isActive) {
			return;
		}

		if (!hasMinimumPlayers()) {
			stopCountdown();
		}
	}

	private void tickCountdown() {
		if (!isActive || game.getGameStatus() != GameStatus.WAITING) {
			stopCountdown();
			return;
		}

		if (!hasMinimumPlayers()) {
			stopCountdown();
			return;
		}

		remainingSeconds--;

		if (shouldAnnounce(remainingSeconds)) {
			announceCountdown();
		}

		if (remainingSeconds <= 0) {
			finishCountdown();
		}
	}

	private void finishCountdown() {
		isActive = false;
		if (countdownTask != null) {
			countdownTask.cancel();
			countdownTask = null;
		}

		Logger.info("Countdown finished for game {}, starting game", game.getGameId());
		game.startGame();
	}

	private void announceCountdown() {
		Component message = createCountdownMessage();
		game.getPlayers().forEach(player -> player.sendMessage(message));
	}

	private Component createCountdownMessage() {
		if (remainingSeconds > 1) {
			return Component.text("Game starting in ")
					.color(NamedTextColor.YELLOW)
					.append(Component.text(remainingSeconds)
							.color(NamedTextColor.RED))
					.append(Component.text(" seconds!")
							.color(NamedTextColor.YELLOW));
		} else if (remainingSeconds == 1) {
			return Component.text("Game starting in ")
					.color(NamedTextColor.YELLOW)
					.append(Component.text("1")
							.color(NamedTextColor.RED))
					.append(Component.text(" second!")
							.color(NamedTextColor.YELLOW));
		} else {
			return Component.text("Game starting now!")
					.color(NamedTextColor.GREEN);
		}
	}

	private boolean shouldAnnounce(int seconds) {
		for (int time : ANNOUNCEMENT_TIMES) {
			if (seconds == time) {
				return true;
			}
		}
		return false;
	}

	private boolean hasMinimumPlayers() {
		int teamSize = game.getBedwarsGameType().getTeamSize();
		if (teamSize <= 0) teamSize = 1;

		int minPlayersRequired = teamSize * Math.min(2, game.getMapEntry().getConfiguration().getTeams().size());
		return game.getPlayers().size() >= minPlayersRequired;
	}

}
