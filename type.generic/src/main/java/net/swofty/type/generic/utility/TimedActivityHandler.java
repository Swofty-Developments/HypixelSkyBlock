package net.swofty.type.generic.utility;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TimedActivityHandler<T extends HypixelPlayer> {
    private static final Map<UUID, TimedActivityHandler<?>> ACTIVE_HANDLERS = new HashMap<>();

    private final T player;
    private final int countdownSeconds;
    private final int activityDurationSeconds;
    private final BiConsumer<T, Integer> onCountdownTick;
    private final Consumer<T> onActivityStart;
    private final BiConsumer<T, Integer> onActivityTick;
    private final Consumer<T> onActivityComplete;
    private final Consumer<T> onActivityCancelled;

    @Getter
    private ActivityState state = ActivityState.IDLE;
    private int currentCountdown;
    @Getter
    private int elapsedActivityTime;
    private Task scheduledTask;

    public enum ActivityState {
        IDLE,
        COUNTING_DOWN,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    private TimedActivityHandler(Builder<T> builder) {
        this.player = builder.player;
        this.countdownSeconds = builder.countdownSeconds;
        this.activityDurationSeconds = builder.activityDurationSeconds;
        this.onCountdownTick = builder.onCountdownTick;
        this.onActivityStart = builder.onActivityStart;
        this.onActivityTick = builder.onActivityTick;
        this.onActivityComplete = builder.onActivityComplete;
        this.onActivityCancelled = builder.onActivityCancelled;
    }

    @SuppressWarnings("unchecked")
    public static <T extends HypixelPlayer> TimedActivityHandler<T> getActiveHandler(T player) {
        return (TimedActivityHandler<T>) ACTIVE_HANDLERS.get(player.getUuid());
    }

    public static boolean hasActiveHandler(HypixelPlayer player) {
        TimedActivityHandler<?> handler = ACTIVE_HANDLERS.get(player.getUuid());
        return handler != null && (handler.state == ActivityState.COUNTING_DOWN || handler.state == ActivityState.IN_PROGRESS);
    }

    public void start() {
        if (state != ActivityState.IDLE) {
            return;
        }

        TimedActivityHandler<?> existing = ACTIVE_HANDLERS.get(player.getUuid());
        if (existing != null && (existing.state == ActivityState.COUNTING_DOWN || existing.state == ActivityState.IN_PROGRESS)) {
            existing.cancel();
        }

        ACTIVE_HANDLERS.put(player.getUuid(), this);
        currentCountdown = countdownSeconds;
        elapsedActivityTime = 0;

        if (countdownSeconds > 0) {
            state = ActivityState.COUNTING_DOWN;
            scheduleCountdown();
        } else {
            startActivity();
        }
    }

    private void scheduleCountdown() {
        scheduledTask = MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (state != ActivityState.COUNTING_DOWN) {
                return TaskSchedule.stop();
            }

            if (onCountdownTick != null) {
                onCountdownTick.accept(player, currentCountdown);
            }

            currentCountdown--;

            if (currentCountdown < 0) {
                startActivity();
                return TaskSchedule.stop();
            }

            return TaskSchedule.seconds(1);
        });
    }

    private void startActivity() {
        state = ActivityState.IN_PROGRESS;
        elapsedActivityTime = 0;

        if (onActivityStart != null) {
            onActivityStart.accept(player);
        }

        if (activityDurationSeconds > 0) {
            scheduleActivityTicks();
        }
    }

    private void scheduleActivityTicks() {
        scheduledTask = MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (state != ActivityState.IN_PROGRESS) {
                return TaskSchedule.stop();
            }

            elapsedActivityTime++;

            if (onActivityTick != null) {
                onActivityTick.accept(player, elapsedActivityTime);
            }

            if (elapsedActivityTime >= activityDurationSeconds) {
                complete();
                return TaskSchedule.stop();
            }

            return TaskSchedule.seconds(1);
        });
    }

    public void complete() {
        if (state != ActivityState.IN_PROGRESS && state != ActivityState.COUNTING_DOWN) {
            return;
        }

        state = ActivityState.COMPLETED;

        if (scheduledTask != null) {
            scheduledTask.cancel();
            scheduledTask = null;
        }

        if (onActivityComplete != null) {
            onActivityComplete.accept(player);
        }

        ACTIVE_HANDLERS.remove(player.getUuid());
    }

    public void cancel() {
        if (state != ActivityState.COUNTING_DOWN && state != ActivityState.IN_PROGRESS) {
            return;
        }

        state = ActivityState.CANCELLED;

        if (scheduledTask != null) {
            scheduledTask.cancel();
            scheduledTask = null;
        }

        if (onActivityCancelled != null) {
            onActivityCancelled.accept(player);
        }

        ACTIVE_HANDLERS.remove(player.getUuid());
    }

    public int getRemainingTime() {
        if (state == ActivityState.COUNTING_DOWN) {
            return currentCountdown;
        } else if (state == ActivityState.IN_PROGRESS) {
            return activityDurationSeconds - elapsedActivityTime;
        }
        return 0;
    }

    public boolean isCountingDown() {
        return state == ActivityState.COUNTING_DOWN;
    }

    public boolean isInProgress() {
        return state == ActivityState.IN_PROGRESS;
    }

    public boolean isActive() {
        return state == ActivityState.COUNTING_DOWN || state == ActivityState.IN_PROGRESS;
    }

    public static <T extends HypixelPlayer> Builder<T> builder(T player) {
        return new Builder<>(player);
    }

    public static class Builder<T extends HypixelPlayer> {
        private final T player;
        private int countdownSeconds = 5;
        private int activityDurationSeconds = 0;
        private BiConsumer<T, Integer> onCountdownTick;
        private Consumer<T> onActivityStart;
        private BiConsumer<T, Integer> onActivityTick;
        private Consumer<T> onActivityComplete;
        private Consumer<T> onActivityCancelled;

        private Builder(T player) {
            this.player = player;
        }

        public Builder<T> countdownSeconds(int seconds) {
            this.countdownSeconds = seconds;
            return this;
        }

        public Builder<T> activityDurationSeconds(int seconds) {
            this.activityDurationSeconds = seconds;
            return this;
        }

        public Builder<T> onCountdownTick(BiConsumer<T, Integer> callback) {
            this.onCountdownTick = callback;
            return this;
        }

        public Builder<T> onActivityStart(Consumer<T> callback) {
            this.onActivityStart = callback;
            return this;
        }

        public Builder<T> onActivityTick(BiConsumer<T, Integer> callback) {
            this.onActivityTick = callback;
            return this;
        }

        public Builder<T> onActivityComplete(Consumer<T> callback) {
            this.onActivityComplete = callback;
            return this;
        }

        public Builder<T> onActivityCancelled(Consumer<T> callback) {
            this.onActivityCancelled = callback;
            return this;
        }

        public TimedActivityHandler<T> build() {
            return new TimedActivityHandler<>(this);
        }
    }
}