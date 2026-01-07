package net.swofty.type.skywarsgame.game;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;

public class SkywarsGameCountdown {
    private final SkywarsGame game;
    @Getter
    private boolean active = false;
    private Task countdownTask;
    @Getter
    private int secondsRemaining;

    public SkywarsGameCountdown(SkywarsGame game) {
        this.game = game;
    }

    public void startCountdown() {
        if (active) return;
        active = true;
        secondsRemaining = 30;
        game.setGameStatus(SkywarsGameStatus.STARTING);

        countdownTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (secondsRemaining <= 0) {
                active = false;
                countdownTask.cancel();
                game.startGame();
                return;
            }

            if (secondsRemaining <= 5 || secondsRemaining == 10 || secondsRemaining == 15 || secondsRemaining == 20 || secondsRemaining == 30) {
                broadcastCountdownMessage(secondsRemaining);

                if (secondsRemaining <= 5) {
                    showCountdownTitle(secondsRemaining);
                }
            }

            secondsRemaining--;
        }).repeat(TaskSchedule.seconds(1)).schedule();
    }

    private void broadcastCountdownMessage(int seconds) {
        String word = seconds == 1 ? "second" : "seconds";

        NamedTextColor numberColor;
        if (seconds <= 5) {
            numberColor = NamedTextColor.RED;
        } else if (seconds == 10) {
            numberColor = NamedTextColor.GOLD;
        } else {
            numberColor = NamedTextColor.GREEN;
        }

        Component message = Component.text("The game starts in ", NamedTextColor.YELLOW)
                .append(Component.text(seconds, numberColor))
                .append(Component.text(" " + word + "!", NamedTextColor.YELLOW));

        game.broadcastMessage(message);
    }

    private void showCountdownTitle(int seconds) {
        NamedTextColor color = NamedTextColor.RED;

        Title title = Title.title(
                Component.text(String.valueOf(seconds), color),
                Component.empty(),
                Title.Times.times(Duration.ZERO, Duration.ofMillis(900), Duration.ofMillis(100))
        );

        game.getPlayers().forEach(p -> p.showTitle(title));
    }

    public void forceStart(int seconds) {
        if (active) {
            countdownTask.cancel();
        }
        active = true;
        secondsRemaining = seconds;
        game.setGameStatus(SkywarsGameStatus.STARTING);

        countdownTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (secondsRemaining <= 0) {
                active = false;
                countdownTask.cancel();
                game.startGame();
                return;
            }

            broadcastCountdownMessage(secondsRemaining);
            showCountdownTitle(secondsRemaining);
            secondsRemaining--;
        }).repeat(TaskSchedule.seconds(1)).schedule();
    }

    public void checkCountdownConditions() {
        if (!active) return;

        if (!game.hasMinimumPlayers()) {
            cancelCountdown();
        }
    }

    private void cancelCountdown() {
        if (!active) return;
        active = false;
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        game.setGameStatus(SkywarsGameStatus.WAITING);
        game.broadcastMessage(Component.text("Not enough players! Countdown cancelled.", NamedTextColor.RED));
    }
}
