package net.swofty.type.replayviewer.playback;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.game.replay.ReplayMetadata;
import net.swofty.type.game.replay.entity.EntityStateTracker;
import net.swofty.type.game.replay.recordable.Recordable;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.entity.ReplayEntity;
import net.swofty.type.replayviewer.entity.ReplayEntityManager;
import net.swofty.type.replayviewer.playback.display.DynamicTextManager;
import net.swofty.type.replayviewer.playback.npc.NpcReplayManager;
import net.swofty.type.replayviewer.playback.scoreboard.GenericReplayScoreboard;
import net.swofty.type.replayviewer.playback.scoreboard.ReplayScoreboard;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Getter
public class ReplaySession {
    private final UUID viewerId;

    // TODO: support multiple viewers in the same session on initial join
    //  and those who join the same session later, for example from a /party warp
    private final Player viewer;
    private final ReplayMetadata metadata;
    private final InstanceContainer instance;

    private final ReplayEntityManager entityManager;
    private final EntityStateTracker stateTracker;
    private final ReplayData replayData;

    private final DroppedItemManager droppedItemManager;
    private final DynamicTextManager dynamicTextManager;
    private final NpcReplayManager npcManager;
    private final ReplayScoreboard scoreboard;

    private volatile int currentTick = 0;
    private volatile boolean playing = false;
    private volatile float playbackSpeed = 1.0f;
    private volatile int skipSeconds = 30;

    private Task playbackTask;
    private Integer spectatingEntityId = null;

    public static final float[] SPEED_PRESETS = {0.25f, 0.5f, 1.0f, 2.0f, 4.0f};
    public static final short[] SKIP_PRESETS = {1, 5, 10, 30, 60};

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

        this.droppedItemManager = new DroppedItemManager(this);
        this.dynamicTextManager = new DynamicTextManager(this);
        this.npcManager = new NpcReplayManager(this);

        this.scoreboard = new GenericReplayScoreboard(this);
        this.scoreboard.create(viewer);

        viewer.setGameMode(GameMode.ADVENTURE);
        viewer.setFlying(true);
        viewer.setAllowFlying(true);
        viewer.setInvisible(true);
    }

    public void play() {
        if (playing) return;
        playing = true;

        // give inventory controls
        TypeReplayViewerLoader.populateInventory((HypixelPlayer) getViewer());

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
    }

    public void pause() {
        if (!playing) return;
        playing = false;

        if (playbackTask != null) {
            playbackTask.cancel();
            playbackTask = null;
        }
    }

    public void togglePlayPause() {
        if (playing) {
            pause();
        } else {
            play();
        }
    }

    public void stop() {
        pause();
        entityManager.cleanup();

        droppedItemManager.clear();
        dynamicTextManager.cleanup();
        npcManager.cleanup();
        scoreboard.remove(viewer);

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (instance.getPlayers().isEmpty()) {
                MinecraftServer.getInstanceManager().unregisterInstance(instance);
            }
        }).delay(TaskSchedule.seconds(5)).schedule();

        Logger.info("Replay session stopped for {}", viewerId);
    }

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

        droppedItemManager.seekTo(targetTick);
        dynamicTextManager.seekTo(targetTick);
        scoreboard.update(this);

        if (wasPlaying) {
            play();
        }

        showSeekTitle(targetTick);
    }

    public void skipForward(int seconds) {
        seekTo(currentTick + seconds * 20);
    }

    public void skipBackward(int seconds) {
        seekTo(currentTick - seconds * 20);
    }

    public void setPlaybackSpeed(float speed) {
        this.playbackSpeed = Math.max(0.25f, Math.min(4.0f, speed));
        if (playing) {
            pause();
            play();
        }
        viewer.sendMessage(Component.text("Speed: " + playbackSpeed + "x", NamedTextColor.AQUA));
    }

    public int getTotalTicks() {
        return metadata.getDurationTicks();
    }

    public String getFormattedTime() {
        int seconds = currentTick / 20;
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }

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

        droppedItemManager.tick(tick);
        dynamicTextManager.tick(tick);
        npcManager.tick();
        updateActionBar();
    }

    private void updateActionBar() {
        // looks like §cPaused    §e00:00 / 01:00    §61.0x
        Component actionBar = Component.text()
            .append(Component.text(playing ? "§aPlaying" : "§cPaused"))
            .append(Component.text("    "))
            .append(Component.text(getFormattedTime() + " / " + getFormattedTotalTime(), NamedTextColor.YELLOW))
            .append(Component.text("    "))
            .append(Component.text(String.format("%.1fx", playbackSpeed), NamedTextColor.GOLD))
            .build();
        viewer.sendActionBar(actionBar);
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

    public void followEntity(int entityId) {
        this.spectatingEntityId = entityId;
        net.minestom.server.entity.Entity entity = entityManager.getEntity(entityId);
        if (entity != null) {
            viewer.spectate(entity);

            String name = getEntityDisplayName(entityId);
            viewer.sendMessage(Component.text("Now following: ", NamedTextColor.GRAY)
                .append(Component.text(name, NamedTextColor.YELLOW)));
        }
    }

    public void stopFollowing() {
        this.spectatingEntityId = null;
        viewer.stopSpectating();
        viewer.sendMessage(Component.text("Free camera mode", NamedTextColor.GRAY));
    }

    public void followNextPlayer() {
        List<Integer> playerEntityIds = getPlayerEntityIds();
        if (playerEntityIds.isEmpty()) return;

        int currentIndex = spectatingEntityId != null ? playerEntityIds.indexOf(spectatingEntityId) : -1;
        int nextIndex = (currentIndex + 1) % playerEntityIds.size();
        followEntity(playerEntityIds.get(nextIndex));
    }

    public void followPreviousPlayer() {
        List<Integer> playerEntityIds = getPlayerEntityIds();
        if (playerEntityIds.isEmpty()) return;

        int currentIndex = spectatingEntityId != null ? playerEntityIds.indexOf(spectatingEntityId) : 0;
        int prevIndex = (currentIndex - 1 + playerEntityIds.size()) % playerEntityIds.size();
        followEntity(playerEntityIds.get(prevIndex));
    }

    private List<Integer> getPlayerEntityIds() {
        List<Integer> ids = new java.util.ArrayList<>();
        for (int entityId : entityManager.getEntityIds()) {
            Entity entity = entityManager.getEntity(entityId);
            // Check if it's a player entity type
            if (entity != null && entity.getEntityType() == EntityType.PLAYER) {
                ids.add(entityId);
            }
        }
        return ids;
    }

    public String getEntityDisplayName(int entityId) {
        Entity entity = entityManager.getEntity(entityId);
        if (entity instanceof ReplayEntity replayEntity) {
            UUID uuid = replayEntity.getRecordedUuid();
            String name = metadata.getPlayers().get(uuid);
            if (name != null) return name;
        }
        // TODO: throw error
        return String.valueOf(entityId);
    }

    public short cycleSkip(int previous) {
        for (short preset : SKIP_PRESETS) {
            if (preset > previous) {
                return preset;
            }
        }
        return SKIP_PRESETS[0];
    }

    public void cycleSpeedUp() {
        for (float preset : SPEED_PRESETS) {
            if (preset > playbackSpeed) {
                setPlaybackSpeed(preset);
                return;
            }
        }
        int speed = (int) SPEED_PRESETS[SPEED_PRESETS.length - 1];
        setPlaybackSpeed(speed);
    }

    public void cycleSpeedDown() {
        for (int i = SPEED_PRESETS.length - 1; i >= 0; i--) {
            if (SPEED_PRESETS[i] < playbackSpeed) {
                setPlaybackSpeed(SPEED_PRESETS[i]);
                return;
            }
        }
        setPlaybackSpeed(SPEED_PRESETS[0]);
    }

    public float getProgress() {
        if (getTotalTicks() == 0) return 0;
        return (float) currentTick / getTotalTicks() * 100;
    }

    public void seekToPercent(float percent) {
        int targetTick = (int) (getTotalTicks() * (percent / 100f));
        seekTo(targetTick);
    }

}
