package net.swofty.type.murdermysterygame.game;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;

public class GameCountdown {
    private final Game game;
    @Getter
    private boolean active = false;
    private Task countdownTask;
    @Getter
    private int secondsRemaining = 30;

    public GameCountdown(Game game) {
        this.game = game;
    }

    public void startCountdown() {
        if (active) return;

        active = true;
        secondsRemaining = 30;

        countdownTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!active) return;

            if (secondsRemaining <= 0) {
                active = false;
                game.startGame();
                return;
            }

            // Announce at specific intervals
            if (secondsRemaining == 30 || secondsRemaining == 20 || secondsRemaining == 15 ||
                    secondsRemaining == 10 || secondsRemaining <= 5) {
                announceCountdown(secondsRemaining);
            }

            secondsRemaining--;
        }).repeat(TaskSchedule.seconds(1)).schedule();
    }

    public void stopCountdown() {
        if (!active) return;

        active = false;
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }

        game.getPlayersAsAudience().sendMessage(
                Component.text("Countdown cancelled - not enough players!", NamedTextColor.RED)
        );
    }

    public void checkCountdownConditions() {
        if (active && game.getPlayers().size() < game.getGameType().getMinPlayers()) {
            stopCountdown();
        }
    }

    public void forceStart(int seconds) {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        active = true;
        secondsRemaining = seconds;

        game.getPlayersAsAudience().sendMessage(
                Component.text("Game force started! Starting in " + seconds + " seconds!", NamedTextColor.GREEN)
        );

        countdownTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!active) return;

            if (secondsRemaining <= 0) {
                active = false;
                game.startGame();
                return;
            }

            announceCountdown(secondsRemaining);
            secondsRemaining--;
        }).repeat(TaskSchedule.seconds(1)).schedule();
    }

    private void announceCountdown(int seconds) {
        NamedTextColor numberColor = seconds <= 5 ? NamedTextColor.RED : NamedTextColor.AQUA;
        String word = seconds == 1 ? "second" : "seconds";

        // Format: "The game is starting in X seconds!"
        game.getPlayersAsAudience().sendMessage(
                Component.empty()
                        .append(Component.text("The game is starting in ", NamedTextColor.YELLOW))
                        .append(Component.text(seconds, numberColor))
                        .append(Component.text(" " + word + "!", NamedTextColor.YELLOW))
        );

        if (seconds <= 5) {
            Title title = Title.title(
                    Component.text(String.valueOf(seconds), numberColor),
                    Component.empty(),
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(800), Duration.ofMillis(200))
            );
            game.getPlayers().forEach(p -> p.showTitle(title));
        }
    }
}
