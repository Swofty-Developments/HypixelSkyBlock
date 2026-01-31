package net.swofty.type.replayviewer.playback;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.replay.ReplayMetadata;
import net.swofty.commons.replay.entity.EntityStateTracker;
import net.swofty.commons.replay.recordable.Recordable;
import net.swofty.type.replayviewer.entity.ReplayEntityManager;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Getter
public class ReplaySession {
    private final UUID viewerId;
	private final Player viewer;
	private final ReplayMetadata metadata;
	private final InstanceContainer instance;

    private final ReplayEntityManager entityManager;
    private final EntityStateTracker stateTracker;
    private final ReplayData replayData;

    // Playback state
    private volatile int currentTick = 0;
    private volatile boolean playing = false;
    private volatile float playbackSpeed = 1.0f;

    private Task playbackTask;

    // Spectating
    private Integer spectatingEntityId = null;

    public ReplaySession(
            Player viewer,
            ReplayMetadata metadata,
            InstanceContainer instance,
            ReplayData replayData
    ) {
        this.viewerId = viewer.getUuid();
        this.viewer = viewer;
        this.metadata = metadata;
        this.instance = instance;
        this.replayData = replayData;
        this.entityManager = new ReplayEntityManager(instance);
        this.stateTracker = new EntityStateTracker();

        // Setup viewer
        viewer.setGameMode(GameMode.SPECTATOR);
        viewer.setFlying(true);
        viewer.setAllowFlying(true);
    }

    /**
     * Starts playback.
     */
    public void play() {
        if (playing) return;
        playing = true;

        int tickInterval = Math.max(1, (int) (1 / playbackSpeed));

        playbackTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!playing || !viewer.isOnline()) {
                pause();
                return;
            }

            // Play ticks based on speed
            int ticksToPlay = (int) Math.max(1, playbackSpeed);
            for (int i = 0; i < ticksToPlay && currentTick < getTotalTicks(); i++) {
                playTick(currentTick);
                currentTick++;
            }

            // Check if finished
            if (currentTick >= getTotalTicks()) {
                onReplayEnd();
            }

        }).repeat(TaskSchedule.tick(tickInterval)).schedule();

        viewer.sendMessage(Component.text("▶ Playing replay", NamedTextColor.GREEN));
    }

    /**
     * Pauses playback.
     */
    public void pause() {
        if (!playing) return;
        playing = false;

        if (playbackTask != null) {
            playbackTask.cancel();
            playbackTask = null;
        }

        viewer.sendMessage(Component.text("⏸ Paused", NamedTextColor.YELLOW));
    }

    /**
     * Toggles play/pause.
     */
    public void togglePlayPause() {
        if (playing) {
            pause();
        } else {
            play();
        }
    }

    /**
     * Stops the session and cleans up.
     */
    public void stop() {
        pause();
        entityManager.cleanup();

        // Unregister instance after delay
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (instance.getPlayers().isEmpty()) {
                MinecraftServer.getInstanceManager().unregisterInstance(instance);
            }
        }).delay(TaskSchedule.seconds(5)).schedule();

        Logger.info("Replay session stopped for {}", viewerId);
    }

    /**
     * Seeks to a specific tick.
     */
    public void seekTo(int targetTick) {
        boolean wasPlaying = playing;
        pause();

        targetTick = Math.max(0, Math.min(targetTick, getTotalTicks() - 1));

        if (targetTick > currentTick) {
            seekForward(targetTick);
        } else if (targetTick < currentTick) {
            seekBackward(targetTick);
        }

        currentTick = targetTick;

        if (wasPlaying) {
            play();
        }

        showSeekTitle(targetTick);
    }

    /**
     * Skips forward by seconds.
     */
    public void skipForward(int seconds) {
        seekTo(currentTick + seconds * 20);
    }

    /**
     * Skips backward by seconds.
     */
    public void skipBackward(int seconds) {
        seekTo(currentTick - seconds * 20);
    }

    /**
     * Sets playback speed.
     */
    public void setSpeed(float speed) {
        this.playbackSpeed = Math.max(0.25f, Math.min(4.0f, speed));
        if (playing) {
            pause();
            play();
        }
        viewer.sendMessage(Component.text("Speed: " + playbackSpeed + "x", NamedTextColor.AQUA));
    }

    /**
     * Gets total ticks in this replay.
     */
    public int getTotalTicks() {
        return metadata.getDurationTicks();
    }

    /**
     * Gets formatted current time.
     */
    public String getFormattedTime() {
        int seconds = currentTick / 20;
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }

    /**
     * Gets formatted total time.
     */
    public String getFormattedTotalTime() {
        int seconds = getTotalTicks() / 20;
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }

    private void playTick(int tick) {
        List<Recordable> recordables = replayData.getRecordablesAt(tick);
        for (Recordable recordable : recordables) {
            try {
                RecordablePlayer.play(recordable, this);
            } catch (Exception e) {
                Logger.error(e, "Failed to play recordable at tick {}", tick);
            }
        }
    }

    private void seekForward(int targetTick) {
        // Collect entity states and block changes between current and target
        ReplaySeeker.seekForward(this, currentTick, targetTick);
    }

    private void seekBackward(int targetTick) {
        // Reset and replay from beginning or last checkpoint
        ReplaySeeker.seekBackward(this, currentTick, targetTick);
    }

    private void showSeekTitle(int tick) {
        Title title = Title.title(
                Component.text(getFormattedTime(), NamedTextColor.GREEN),
                Component.text("/" + getFormattedTotalTime(), NamedTextColor.GRAY),
                Title.Times.times(Duration.ZERO, Duration.ofMillis(500), Duration.ofMillis(200))
        );
        viewer.showTitle(title);
    }

    private void onReplayEnd() {
        pause();
        viewer.showTitle(Title.title(
                Component.text("Replay Ended", NamedTextColor.GOLD),
                Component.text("Use /replay restart to watch again", NamedTextColor.GRAY),
                Title.Times.times(Duration.ofMillis(200), Duration.ofSeconds(3), Duration.ofMillis(500))
        ));
    }

}
