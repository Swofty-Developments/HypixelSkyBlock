package net.swofty.type.bedwarsgame.replay;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.protocol.objects.replay.ReplayDataBatchProtocolObject;
import net.swofty.commons.protocol.objects.replay.ReplayStartProtocolObject;
import net.swofty.commons.replay.ReplayRecorder;
import net.swofty.commons.replay.dispatcher.BlockChangeDispatcher;
import net.swofty.commons.replay.dispatcher.DispatcherManager;
import net.swofty.commons.replay.dispatcher.EntityLocationDispatcher;
import net.swofty.commons.replay.recordable.RecordableBlockChange;
import net.swofty.commons.replay.recordable.RecordableCustomEvent;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// TODO: everything about this file is slop
public class BedWarsReplayManager {
    private static final int BATCH_INTERVAL_SECONDS = 10;

    private final BedWarsGame game;
    private final ProxyService replayService;

    @Getter
    private final ReplayRecorder recorder;
    @Getter
    private final DispatcherManager dispatchers;

    private Task tickTask;
    private Task batchTask;
    private int batchIndex = 0;

    @Getter
    private boolean recording = false;

    public BedWarsReplayManager(BedWarsGame game, ProxyService replayService) {
        this.game = game;
        this.replayService = replayService;
        this.recorder = new ReplayRecorder(game.getGameId(), ServerType.BEDWARS_GAME, this::sendToService);
        this.dispatchers = new DispatcherManager(recorder);
    }

    /**
     * Sends replay data to the replay service.
     */
    private void sendToService(Object data) {
        if (replayService == null) {
            Logger.debug("No replay service configured, skipping: {}", data.getClass().getSimpleName());
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                replayService.handleRequest(data).exceptionally(e -> {
                    Logger.error(e, "Failed to send replay data to service");
                    return null;
                });
            } catch (Exception e) {
                Logger.error(e, "Failed to send replay data to service");
            }
        });
    }

    /**
     * Starts recording the game.
     */
    public void startRecording() {
        if (recording) return;
        recording = true;

        // Set map center for coordinate optimization
        var locations = game.getMapEntry().getConfiguration().getLocations();
        if (locations.getWaiting() != null) {
            recorder.setMapCenter(locations.getWaiting().x(), locations.getWaiting().z());
        }

        // Collect player info
        Map<UUID, String> players = new HashMap<>();
        game.getPlayers().forEach(p -> players.put(p.getUuid(), p.getUsername()));

        // Collect team info
        Map<String, List<UUID>> teams = new HashMap<>();
        Map<String, ReplayStartProtocolObject.TeamInfo> teamInfo = new HashMap<>();

        for (BedWarsTeam team : game.getTeams()) {
            String teamId = team.getTeamKey().name();
            List<UUID> teamPlayers = new ArrayList<>(team.getPlayerIds());
            teams.put(teamId, teamPlayers);

            teamInfo.put(teamId, new ReplayStartProtocolObject.TeamInfo(
                    team.getName(),
                    team.getColorCode(),
                    team.getTeamKey().rgb()
            ));
        }

        // Generate map hash based on map name and modification time
        String mapName = game.getMapEntry().getName();
        String mapHash = generateMapHash(mapName);

        // Start recording session
        recorder.start(
                game.getGameType().name(),
                mapName,
                mapHash,
                players,
                teams,
                teamInfo
        );

        // Register dispatchers
        InstanceContainer instance = game.getInstance();
        dispatchers.register(new EntityLocationDispatcher(instance));
        dispatchers.register(new BlockChangeDispatcher(instance));
        dispatchers.register(new BedWarsEventDispatcher(game, recorder));

        // Start tick task (every tick for accurate timing)
        tickTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!recording) return;
            recorder.tick();
            dispatchers.tick();
        }).repeat(TaskSchedule.tick(1)).schedule();

        // Start batch sending task (every 10 seconds)
        batchTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!recording) return;
            sendCurrentBatch();
        }).delay(TaskSchedule.seconds(BATCH_INTERVAL_SECONDS))
          .repeat(TaskSchedule.seconds(BATCH_INTERVAL_SECONDS))
          .schedule();

        Logger.info("Started replay recording for game {} (map: {})", game.getGameId(), mapName);
    }

    /**
     * Sends the current batch of recorded data to the service.
     */
    private void sendCurrentBatch() {
        byte[] batchData = recorder.flushBatch();
        if (batchData == null || batchData.length == 0) return;

        ReplayDataBatchProtocolObject.BatchMessage batch = new ReplayDataBatchProtocolObject.BatchMessage(
                UUID.fromString(game.getGameId()),
                batchIndex++,
                recorder.getStartTick(),
                recorder.getCurrentTick(),
                recorder.getRecordableCount(),
                batchData
        );

        sendToService(batch);
        Logger.debug("Sent replay batch {} ({} bytes) for game {}", batchIndex - 1, batchData.length, game.getGameId());
    }

    /**
     * Stops recording and finalizes the replay.
     */
    public void stopRecording(String winnerId, String winnerType) {
        if (!recording) return;
        recording = false;

        if (tickTask != null) {
            tickTask.cancel();
            tickTask = null;
        }

        if (batchTask != null) {
            batchTask.cancel();
            batchTask = null;
        }

        // Send final batch
        sendCurrentBatch();

        dispatchers.cleanup();
        recorder.finish(winnerId, winnerType);

        Logger.info("Stopped replay recording for game {} (winner: {})", game.getGameId(), winnerId);
    }

    public void recordBedDestroyed(TeamKey teamKey, BedWarsPlayer destroyer) {
        if (!recording) return;

        // Record as custom event
        Map<String, String> eventData = new HashMap<>();
        eventData.put("type", "BED_DESTROYED");
        eventData.put("team", teamKey.name());
        eventData.put("destroyer", destroyer != null ? destroyer.getUuid().toString() : "ADMIN");
        eventData.put("destroyerName", destroyer != null ? destroyer.getUsername() : "Administrator");

        recorder.record(new RecordableCustomEvent("bed_destroyed", eventData));

        // Also record block changes for the bed location
        var team = game.getMapEntry().getConfiguration().getTeams().get(teamKey);
        if (team != null && team.getBed() != null) {
            var bedPos = team.getBed();
            if (bedPos.feet() != null) {
                recorder.record(new RecordableBlockChange(
                        (int) bedPos.feet().x(), (int) bedPos.feet().y(), (int) bedPos.feet().z(),
                        Block.AIR.stateId(), Block.RED_BED.stateId()
                ));
            }
            if (bedPos.head() != null) {
                recorder.record(new RecordableBlockChange(
                        (int) bedPos.head().x(), (int) bedPos.head().y(), (int) bedPos.head().z(),
                        Block.AIR.stateId(), Block.RED_BED.stateId()
                ));
            }
        }
    }

    public void recordBedRespawned(TeamKey teamKey) {
        if (!recording) return;

        Map<String, String> eventData = new HashMap<>();
        eventData.put("type", "BED_RESPAWNED");
        eventData.put("team", teamKey.name());

        recorder.record(new RecordableCustomEvent("bed_respawned", eventData));
    }

    public void recordAdminAction(String adminName, String action, String details) {
        if (!recording) return;

        Map<String, String> eventData = new HashMap<>();
        eventData.put("type", "ADMIN_ACTION");
        eventData.put("admin", adminName);
        eventData.put("action", action);
        eventData.put("details", details);

        recorder.record(new RecordableCustomEvent("admin_action", eventData));
    }

    public void recordKill(BedWarsPlayer killer, BedWarsPlayer victim, boolean isFinalKill) {
        if (!recording) return;

        Map<String, String> eventData = new HashMap<>();
        eventData.put("type", isFinalKill ? "FINAL_KILL" : "KILL");
        eventData.put("killer", killer.getUuid().toString());
        eventData.put("killerName", killer.getUsername());
        eventData.put("victim", victim.getUuid().toString());
        eventData.put("victimName", victim.getUsername());

        recorder.record(new RecordableCustomEvent(isFinalKill ? "final_kill" : "kill", eventData));
    }

    public BlockChangeDispatcher getBlockChangeDispatcher() {
        return dispatchers.getDispatcher(BlockChangeDispatcher.class);
    }

    public BedWarsEventDispatcher getBedWarsEventDispatcher() {
        return dispatchers.getDispatcher(BedWarsEventDispatcher.class);
    }

    private String generateMapHash(String mapName) {
        // TODO: hash
        return mapName.toLowerCase().replace(" ", "_");
    }
}
