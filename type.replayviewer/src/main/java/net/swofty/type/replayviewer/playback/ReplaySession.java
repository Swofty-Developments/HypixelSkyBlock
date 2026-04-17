package net.swofty.type.replayviewer.playback;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.EntityEquipmentPacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.network.packet.server.play.UpdateScorePacket;
import net.minestom.server.scoreboard.BelowNameTag;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.game.replay.ReplayMetadata;
import net.swofty.type.game.replay.entity.EntityStateTracker;
import net.swofty.type.game.replay.recordable.Recordable;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.ScheduleUtility;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.entity.ReplayEntity;
import net.swofty.type.replayviewer.entity.ReplayEntityManager;
import net.swofty.type.replayviewer.entity.ReplayPlayerEntity;
import net.swofty.type.replayviewer.playback.display.DynamicTextManager;
import net.swofty.type.replayviewer.playback.npc.NpcReplayManager;
import net.swofty.type.replayviewer.playback.scoreboard.GenericReplayScoreboard;
import net.swofty.type.replayviewer.playback.scoreboard.ReplayScoreboard;
import net.swofty.type.replayviewer.util.ReplaySettingsUtil;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ReplaySession {
    private final UUID replayId;
    private final Set<Player> viewers = ConcurrentHashMap.newKeySet();
    private final Map<UUID, ReplayScoreboard> viewerScoreboards = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> viewerSpectating = new ConcurrentHashMap<>();
    private final ReplayMetadata metadata;
    private final InstanceContainer instance;

    private final ReplayEntityManager entityManager;
    private final EntityStateTracker stateTracker;
    private final ReplayData replayData;

    private final DroppedItemManager droppedItemManager;
    private final DynamicTextManager dynamicTextManager;
    private final NpcReplayManager npcManager;
    private final BelowNameTag belowNameTag = new BelowNameTag("health", Component.text("§c❤"));
    private final Map<Integer, PlayerNameTag> playerNameTags = new ConcurrentHashMap<>();

    private volatile int currentTick = 0;
    private volatile boolean playing = false;
    private volatile boolean rebuildingState = false;
    private volatile float playbackSpeed = 1.0f;
    private volatile int skipSeconds = 30;

    private Task playbackTask;

    public static final float[] SPEED_PRESETS = {0.25f, 0.5f, 1.0f, 2.0f, 4.0f};
    public static final short[] SKIP_PRESETS = {1, 5, 10, 30, 60};

    public ReplaySession(
        Player initialViewer,
        ReplayMetadata metadata,
        InstanceContainer instance,
        ReplayData replayData
    ) {
        this.replayId = metadata.getReplayId();
        this.metadata = metadata;
        this.instance = instance;
        this.replayData = replayData;
        this.entityManager = new ReplayEntityManager(instance);
        this.stateTracker = new EntityStateTracker();

        this.droppedItemManager = new DroppedItemManager(this);
        this.dynamicTextManager = new DynamicTextManager(this);
        this.npcManager = new NpcReplayManager(this);

        ReplaySeeker.rebuildStateAtTick(this, 0);

        addViewer(initialViewer);
    }

    public void addViewer(Player viewer) {
        viewers.add(viewer);

        viewer.setGameMode(GameMode.ADVENTURE);
        viewer.setFlying(true);
        viewer.setAllowFlying(true);
        ReplaySettingsUtil.applyVisualSettings((HypixelPlayer) viewer);

        ReplayScoreboard scoreboard = new GenericReplayScoreboard(this);
        scoreboard.create(viewer);
        viewerScoreboards.put(viewer.getUuid(), scoreboard);

        belowNameTag.addViewer(viewer);
        replayBelowNameScores();
        replayNameTags(viewer);

        TypeReplayViewerLoader.populateInventory((HypixelPlayer) viewer);
        TypeReplayViewerLoader.registerSession(viewer.getUuid(), this);
        sendEquipmentSync(viewer);
        ScheduleUtility.delay(() -> sendEquipmentSync(viewer), 1);
        updateActionBar();

        ScheduleUtility.delay(() -> autoFollowForViewer(viewer), 20);


        Logger.info("Added viewer {} to replay session {}", viewer.getUsername(), replayId);
    }

    public void removeViewer(Player viewer) {
        viewers.remove(viewer);
        viewerSpectating.remove(viewer.getUuid());

        belowNameTag.removeViewer(viewer);

        ReplayScoreboard scoreboard = viewerScoreboards.remove(viewer.getUuid());
        if (scoreboard != null) {
            scoreboard.remove(viewer);
        }

        TypeReplayViewerLoader.removeSession(viewer.getUuid());

        Logger.info("Removed viewer {} from replay session {}", viewer.getUsername(), replayId);

        // If no viewers left, stop the session
        if (viewers.isEmpty()) {
            stop();
        }
    }

    public void updateBelowNameScore(int entityId, int score) {
        Entity entity = entityManager.getEntity(entityId);
        if (entity instanceof ReplayPlayerEntity playerEntity) {
            playerEntity.setBelowScore(score);
            sendBelowNameScore(playerEntity.getScoreboardEntryName(), score);
        }
    }

    public void applyPlayerDisplayName(int entityId, String displayName, String prefix, String suffix, int nameColor) {
        Entity entity = entityManager.getEntity(entityId);
        String entryName = displayName;
        if (entity instanceof ReplayPlayerEntity playerEntity) {
            entryName = playerEntity.getScoreboardEntryName();
        }
        PlayerNameTag tag = new PlayerNameTag(entryName, prefix, suffix, nameColor);
        playerNameTags.put(entityId, tag);
        sendNameTagTeam(entityId, tag, null);
    }

    public void autoFollowForViewer(Player viewer) {
        UUID viewerUuid = viewer.getUuid();

        if (!metadata.getPlayers().containsKey(viewerUuid)) {
            return;
        }

        for (int entityId : entityManager.getEntityIds()) {
            Entity entity = entityManager.getEntity(entityId);
            if (entity instanceof ReplayPlayerEntity playerEntity) {
                if (viewerUuid.equals(playerEntity.getActualUuid())) {
                    viewer.teleport(playerEntity.getPosition());
                    viewer.sendMessage("§aTeleporting you to " + playerEntity.getActualUuid());
                    applyTeamGlow(viewer, entity, entityId);
                    return;
                }
            }
        }
    }

    public void play() {
        if (playing) return;
        playing = true;

        int tickInterval = Math.max(1, (int) (1 / playbackSpeed));

        playbackTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!playing || viewers.isEmpty() || viewers.stream().noneMatch(Player::isOnline)) {
                pause();
                return;
            }

            // Play ticks based on speed
            int ticksToPlay = (int) Math.max(1, playbackSpeed);
            for (int i = 0; i < ticksToPlay && currentTick <= getTotalTicks(); i++) {
                playTick(currentTick);
                currentTick++; // check if this is fine
            }

            // Check if finished
            if (currentTick > getTotalTicks()) {
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

        for (var entry : viewerScoreboards.entrySet()) {
            viewers.stream()
                .filter(v -> v.getUuid().equals(entry.getKey()))
                .findFirst().ifPresent(viewer -> entry.getValue().remove(viewer));
        }
        viewerScoreboards.clear();

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (instance.getPlayers().isEmpty()) {
                MinecraftServer.getInstanceManager().unregisterInstance(instance);
            }
        }).delay(TaskSchedule.seconds(5)).schedule();

        Logger.info("Replay session stopped for replay {}", replayId);
    }

    public void seekTo(int targetTick) {
        boolean wasPlaying = playing;
        pause();

        targetTick = Math.clamp(targetTick, 0, getTotalTicks());

        if (targetTick <= currentTick) {
            playerNameTags.clear();
        }

        if (targetTick > currentTick) {
            seekForward(targetTick);
        } else if (targetTick < currentTick) {
            seekBackward(targetTick);
        } else {
            ReplaySeeker.rebuildStateAtTick(this, targetTick);
        }

        currentTick = targetTick;

        droppedItemManager.seekTo(targetTick);
        dynamicTextManager.seekTo(targetTick);
        replayBelowNameScores();
        reapplyViewerSpectating();

        // Update all viewer scoreboards
        for (var entry : viewerScoreboards.entrySet()) {
            entry.getValue().update(this);
        }

        if (wasPlaying) {
            play();
        }

        updateActionBar();
        showSeekTitle(targetTick);
    }

    public void skipForward(int seconds) {
        seekTo(currentTick + seconds * 20);
    }

    public void skipBackward(int seconds) {
        seekTo(currentTick - seconds * 20);
    }

    public void setPlaybackSpeed(float speed) {
        this.playbackSpeed = Math.clamp(speed, 0.25f, 4.0f);
        if (playing) {
            pause();
            play();
        }
        for (Player viewer : viewers) {
            viewer.sendMessage(Component.text("Speed: " + playbackSpeed + "x", NamedTextColor.AQUA));
        }
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
        List<Recordable> tickData = replayData.getRecordablesAt(tick);
        for (Recordable recordable : tickData) {
            try {
                RecordablePlayer.play(recordable, this);
            } catch (Exception e) {
                Logger.error(e, "Failed to play recordable at tick {}", tick);
            }
        }

        droppedItemManager.tick(tick);
        dynamicTextManager.tick(tick);
        npcManager.tick();

        // TODO: move this to update even when it's not playing, because now we can't ever see paused state
        updateActionBar();
    }

    private void updateActionBar() {
        Component actionBar = Component.text()
            .append(Component.text(playing ? "§aPlaying" : "§cPaused"))
            .append(Component.text("    "))
            .append(Component.text(getFormattedTime() + " / " + getFormattedTotalTime(), NamedTextColor.YELLOW))
            .append(Component.text("    "))
            .append(Component.text(String.format("%.1fx", playbackSpeed), NamedTextColor.GOLD))
            .build();
        for (Player viewer : viewers) {
            viewer.sendActionBar(actionBar);
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

    void beginStateRebuild() {
        rebuildingState = true;
    }

    void endStateRebuild() {
        for (int entityId : entityManager.getEntityIds()) {
            Entity entity = entityManager.getEntity(entityId);
            if (entity != null) {
                entity.setAutoViewable(true);
            }
        }

        for (Player viewer : viewers) {
            sendEquipmentSync(viewer);
        }
        ScheduleUtility.delay(() -> viewers.forEach(this::sendEquipmentSync), 1);

        rebuildingState = false;
    }

    private void showSeekTitle(int tick) {
        Title title = Title.title(
            Component.text(getFormattedTime(), NamedTextColor.GREEN),
            Component.text("/" + getFormattedTotalTime(), NamedTextColor.GRAY),
            Title.Times.times(Duration.ZERO, Duration.ofMillis(500), Duration.ofMillis(200))
        );
        for (Player viewer : viewers) {
            viewer.showTitle(title);
        }
    }

    private void replayBelowNameScores() {
        for (int entityId : entityManager.getEntityIds()) {
            Entity entity = entityManager.getEntity(entityId);
            if (entity instanceof ReplayPlayerEntity playerEntity) {
                int score = playerEntity.getBelowScore();
                if (score >= 0) {
                    sendBelowNameScore(playerEntity.getScoreboardEntryName(), score);
                }
            }
        }
    }

    private void sendBelowNameScore(String entryName, int score) {
        UpdateScorePacket packet = new UpdateScorePacket(
            entryName,
            belowNameTag.getObjectiveName(),
            score,
            null,
            null
        );
        for (Player viewer : viewers) {
            viewer.sendPacket(packet);
        }
    }

    private void replayNameTags(Player viewer) {
        for (var entry : playerNameTags.entrySet()) {
            sendNameTagTeam(entry.getKey(), entry.getValue(), viewer);
        }
    }

    private void sendNameTagTeam(int entityId, PlayerNameTag tag, Player viewer) {
        NamedTextColor teamColor = tag.nameColor >= 0
            ? NamedTextColor.nearestTo(TextColor.color(tag.nameColor))
            : NamedTextColor.WHITE;
        String teamName = "REPLAY_NAME_" + entityId;

        TeamsPacket packet = new TeamsPacket(
            teamName,
            new TeamsPacket.CreateTeamAction(
                Component.empty(),
                (byte) 0x00,
                TeamsPacket.NameTagVisibility.ALWAYS,
                TeamsPacket.CollisionRule.NEVER,
                teamColor,
                Component.text(tag.prefix()),
                Component.text(tag.suffix()),
                new ArrayList<>(List.of(tag.entryName()))
            )
        );

        if (viewer != null) {
            viewer.sendPacket(packet);
        } else {
            for (Player v : viewers) {
                v.sendPacket(packet);
            }
        }
    }

    // TODO: parity
    private void onReplayEnd() {
        pause();
        Title title = Title.title(
            Component.text("Replay Ended", NamedTextColor.GOLD),
            Component.text("Use /replay restart to watch again", NamedTextColor.GRAY),
            Title.Times.times(Duration.ofMillis(200), Duration.ofSeconds(3), Duration.ofMillis(500))
        );
        for (Player viewer : viewers) {
            viewer.showTitle(title);
        }
    }

    public void followEntity(Player viewer, int entityId) {
        viewerSpectating.put(viewer.getUuid(), entityId);
        Entity entity = entityManager.getEntity(entityId);
        if (entity != null) {
            viewer.setGameMode(GameMode.SPECTATOR);
            viewer.spectate(entity);
        }
    }

    public void stopFollowing(Player viewer) {
        viewer.setGameMode(GameMode.ADVENTURE);
        viewer.setFlying(true);
        viewer.setAllowFlying(true);

        viewerSpectating.remove(viewer.getUuid());
        viewer.stopSpectating();
    }

    private void reapplyViewerSpectating() {
        for (Player viewer : viewers) {
            Integer targetEntityId = viewerSpectating.get(viewer.getUuid());
            if (targetEntityId == null) {
                continue;
            }

            Entity entity = entityManager.getEntity(targetEntityId);
            if (entity == null) {
                viewerSpectating.remove(viewer.getUuid());
                viewer.stopSpectating();
                viewer.setGameMode(GameMode.ADVENTURE);
                viewer.setFlying(true);
                viewer.setAllowFlying(true);
                continue;
            }

            viewer.setGameMode(GameMode.SPECTATOR);
            viewer.spectate(entity);
        }
    }

    private void sendEquipmentSync(Player viewer) {
        if (viewer == null || !viewer.isOnline()) {
            return;
        }

        for (int entityId : entityManager.getEntityIds()) {
            Entity entity = entityManager.getEntity(entityId);
            if (!(entity instanceof LivingEntity livingEntity)) {
                continue;
            }

            Map<EquipmentSlot, ItemStack> equipmentBySlot = new EnumMap<>(EquipmentSlot.class);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = livingEntity.getEquipment(slot);
                if (stack != null && !stack.isAir()) {
                    equipmentBySlot.put(slot, stack);
                }
            }

            if (!equipmentBySlot.isEmpty()) {
                viewer.sendPacket(new EntityEquipmentPacket(entity.getEntityId(), equipmentBySlot));
            }
        }
    }

    // todo: sloppy method
    private void applyTeamGlow(Player viewer, Entity entity, int entityId) {
        UUID entityUuid = null;
        if (entity instanceof ReplayPlayerEntity playerEntity) {
            entityUuid = playerEntity.getActualUuid();
        }

        if (entityUuid == null) return;

        String teamId = null;
        for (Map.Entry<String, List<UUID>> entry : metadata.getTeams().entrySet()) {
            if (entry.getValue().contains(entityUuid)) {
                teamId = entry.getKey();
                break;
            }
        }

        if (teamId == null) return;

        ReplayMetadata.TeamInfo teamInfo = metadata.getTeamInfo().get(teamId);
        NamedTextColor teamColor = NamedTextColor.WHITE;
        if (teamInfo != null) {
            teamColor = NamedTextColor.nearestTo(TextColor.color(teamInfo.color()));
        }

        entity.setGlowing(true);

        String teamName = "REPLAY_GLOW_" + entityId;
        ReplayPlayerEntity playerEntity = (ReplayPlayerEntity) entity;
        String entityName = playerEntity.getPlayerName();

        viewer.sendPacket(new TeamsPacket(
            teamName,
            new TeamsPacket.CreateTeamAction(
                Component.empty(),
                (byte) 0x02,
                TeamsPacket.NameTagVisibility.ALWAYS,
                TeamsPacket.CollisionRule.NEVER,
                teamColor,
                Component.empty(),
                Component.empty(),
                new ArrayList<>(List.of(entityName))
            )
        ));
    }

    public String getEntityDisplayName(int entityId) {
        Entity entity = entityManager.getEntity(entityId);
        if (entity instanceof ReplayPlayerEntity playerEntity) {
            return playerEntity.getPlayerName();
        }

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

    private record PlayerNameTag(String entryName, String prefix, String suffix, int nameColor) {
    }

}
