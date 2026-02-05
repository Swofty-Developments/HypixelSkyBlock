package net.swofty.type.bedwarsgame.replay;

import lombok.Getter;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.protocol.objects.replay.ReplayDataBatchProtocolObject;
import net.swofty.commons.protocol.objects.replay.ReplayStartProtocolObject;
import net.swofty.commons.scoreboard.ScoreboardData;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.replay.ReplayRecorder;
import net.swofty.type.game.replay.dispatcher.BlockChangeDispatcher;
import net.swofty.type.game.replay.dispatcher.DispatcherManager;
import net.swofty.type.game.replay.dispatcher.EntityLocationDispatcher;
import net.swofty.type.game.replay.recordable.RecordableBlockChange;
import net.swofty.type.game.replay.recordable.RecordableDroppedItem;
import net.swofty.type.game.replay.recordable.RecordableItemPickup;
import net.swofty.type.game.replay.recordable.RecordablePlayerChat;
import net.swofty.type.game.replay.recordable.RecordablePlayerDisplayName;
import net.swofty.type.game.replay.recordable.RecordablePlayerHealth;
import net.swofty.type.game.replay.recordable.RecordablePlayerSkin;
import net.swofty.type.game.replay.recordable.bedwars.RecordableBedDestruction;
import net.swofty.type.game.replay.recordable.bedwars.RecordableFinalKill;
import net.swofty.type.game.replay.recordable.bedwars.RecordableGeneratorUpgrade;
import net.swofty.type.game.replay.recordable.bedwars.RecordableScoreboardState;
import net.swofty.type.game.replay.recordable.bedwars.RecordableTeamElimination;
import org.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    private Task scoreboardTask;
    private int batchIndex = 0;

    private ScoreboardData lastScoreboardState;

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
        int centerChunkX = 0, centerChunkZ = 0;
        if (locations.getWaiting() != null) {
            recorder.setMapCenter(locations.getWaiting().x(), locations.getWaiting().z());
            centerChunkX = (int) locations.getWaiting().x() >> 4;
            centerChunkZ = (int) locations.getWaiting().z() >> 4;
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

        // Serialize and upload map, get the hash
        String mapName = game.getMapEntry().getName();
        InstanceContainer instance = game.getInstance();
        String mapHash = serializeAndUploadMap(instance, mapName, centerChunkX, centerChunkZ);

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
        dispatchers.register(new EntityLifecycleDispatcher(instance));
        dispatchers.register(new EntityLocationDispatcher(instance));
        dispatchers.register(new BlockChangeDispatcher());

        // Record initial player appearances
        recordInitialPlayerStates();

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

        // Start scoreboard state recording task (every 5 seconds for accurate seek)
        scoreboardTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!recording) return;
            recordScoreboardState();
        }).delay(TaskSchedule.seconds(5))
          .repeat(TaskSchedule.seconds(5))
          .schedule();

        Logger.info("Started replay recording for game {} (map: {})", game.getGameId(), mapName);
    }

    private void recordInitialPlayerStates() {
        for (BedWarsPlayer player : game.getPlayers()) {
            recordPlayerAppearance(player);
        }
    }

    /**
     * Records a player's appearance (skin, display name, health).
     */
    public void recordPlayerAppearance(BedWarsPlayer player) {
        if (!recording) return;

        int entityId = player.getEntityId();
        UUID uuid = player.getUuid();

        // Record skin
        var skin = player.getSkin();
        if (skin != null) {
            recorder.record(new RecordablePlayerSkin(
                entityId, uuid, skin.textures(), skin.signature()
            ));
        }

        // Record display name with team prefix
        BedWarsTeam team = game.getTeam(player.getTeamKey().name()).orElse(null);
        String prefix = team != null ? team.getColorCode() + "[" + team.getTeamKey().name().charAt(0) + "] " : "";
        int nameColor = team != null ? team.getTeamKey().rgb() : -1;

        recorder.record(new RecordablePlayerDisplayName(
            entityId, player.getUsername(), prefix, "", nameColor
        ));

        // Record initial health (use 20 as default max health)
        recorder.record(new RecordablePlayerHealth(
            entityId, player.getHealth(), 20.0f
        ));
    }

    public void recordPlayerHealth(BedWarsPlayer player) {
        if (!recording) return;
        recorder.record(new RecordablePlayerHealth(
            player.getEntityId(), player.getHealth(), 20.0f
        ));
    }

    public void recordBedRespawned(TeamKey teamKey) {
        if (!recording) return;
        // Record the block placement for the bed
        var team = game.getMapEntry().getConfiguration().getTeams().get(teamKey);
        if (team != null && team.getBed() != null) {
            var bedPos = team.getBed();
            if (bedPos.feet() != null) {
                recorder.record(new RecordableBlockChange(
                    (int) bedPos.feet().x(), (int) bedPos.feet().y(), (int) bedPos.feet().z(),
                    Block.RED_BED.stateId(), Block.AIR.stateId()
                ));
            }
            if (bedPos.head() != null) {
                recorder.record(new RecordableBlockChange(
                    (int) bedPos.head().x(), (int) bedPos.head().y(), (int) bedPos.head().z(),
                    Block.RED_BED.stateId(), Block.AIR.stateId()
                ));
            }
        }
    }

    public void recordKill(BedWarsPlayer killer, BedWarsPlayer victim, boolean isFinalKill) {
        if (!recording) return;

        if (isFinalKill) {
            byte deathCause = 0; // Player kill
            recordFinalKill(victim, killer, deathCause);
        }
        // Regular kills are implicitly recorded through player death recordables
    }

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
    public void stopRecording() {
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

        if (scoreboardTask != null) {
            scoreboardTask.cancel();
            scoreboardTask = null;
        }

        // Send final batch
        sendCurrentBatch();

        dispatchers.cleanup();
        recorder.finish();

        Logger.info("Stopped replay recording for game {}", game.getGameId());
    }

    public void recordBedDestroyed(TeamKey teamKey, BedWarsPlayer destroyer) {
        if (!recording) return;

        byte teamId = (byte) teamKey.ordinal();
        int destroyerEntityId = destroyer != null ? destroyer.getEntityId() : -1;
        UUID destroyerUuid = destroyer != null ? destroyer.getUuid() : null;

        // Get bed position
        var team = game.getMapEntry().getConfiguration().getTeams().get(teamKey);
        int bedX = 0, bedY = 0, bedZ = 0;
        if (team != null && team.getBed() != null && team.getBed().feet() != null) {
            bedX = (int) team.getBed().feet().x();
            bedY = (int) team.getBed().feet().y();
            bedZ = (int) team.getBed().feet().z();
        }

        recorder.record(new RecordableBedDestruction(
            teamId, destroyerEntityId, destroyerUuid, bedX, bedY, bedZ
        ));

        // Also record the block change
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

    public void recordFinalKill(BedWarsPlayer victim, BedWarsPlayer killer, byte deathCause) {
        if (!recording) return;

        byte victimTeamId = (byte) victim.getTeamKey().ordinal();

        recorder.record(new RecordableFinalKill(
            victim.getEntityId(),
            victim.getUuid(),
            killer != null ? killer.getEntityId() : -1,
            killer != null ? killer.getUuid() : null,
            victimTeamId,
            deathCause
        ));
    }

    public void recordTeamElimination(TeamKey teamKey) {
        if (!recording) return;
        recorder.record(new RecordableTeamElimination((byte) teamKey.ordinal()));
    }

    public void recordGeneratorUpgrade(byte generatorType, byte tier) {
        if (!recording) return;
        recorder.record(new RecordableGeneratorUpgrade(generatorType, tier));
    }

    public void recordDroppedItem(ItemEntity itemEntity) {
        if (!recording) return;

        try {
            Pos pos = itemEntity.getPosition();
            Vec velocity = itemEntity.getVelocity();
            ItemStack itemStack = itemEntity.getItemStack();

            // Serialize item to NBT bytes
            byte[] itemNbt = serializeItemStack(itemStack);

            // Calculate despawn tick
            int despawnTick = recorder.getCurrentTick() + 6000;

            recorder.record(new RecordableDroppedItem(
                itemEntity.getEntityId(),
                itemEntity.getUuid(),
                pos.x(), pos.y(), pos.z(),
                (float) velocity.x(), (float) velocity.y(), (float) velocity.z(),
                itemNbt,
                10, // pickup delay in ticks
                despawnTick
            ));
        } catch (Exception e) {
            Logger.error(e, "Failed to record dropped item");
        }
    }

    public void recordItemPickup(int itemEntityId, int collectorEntityId) {
        if (!recording) return;
        recorder.record(new RecordableItemPickup(itemEntityId, collectorEntityId));
    }

    public void recordPlayerChat(BedWarsPlayer player, String message, boolean isShout) {
        if (!recording) return;
        recorder.record(new RecordablePlayerChat(
            player.getEntityId(),
            message,
            isShout
        ));
    }

    /**
     * Records a periodic scoreboard state snapshot.
     * Should be called periodically (e.g., every 5 seconds) for accurate seek reconstruction.
     */
    public void recordScoreboardState() {
        if (!recording) return;

        String nextEventName = "";
        int nextEventSeconds = 0;

        var eventManager = game.getGameEventManager();
        if (eventManager != null) {
            var currentEvent = eventManager.getCurrentEvent();
            if (currentEvent != null) {
                var nextPhase = currentEvent.next();
                if (nextPhase != currentEvent) {
                    nextEventName = nextPhase.getDisplayName();
                } else {
                    nextEventName = currentEvent.getDisplayName();
                }
                nextEventSeconds = (int) eventManager.getSecondsUntilNextEvent();
            }
        }

        List<RecordableScoreboardState.TeamScoreboardState> teamStates = new ArrayList<>();
        for (BedWarsTeam team : game.getTeams()) {
            int alivePlayers = (int) game.getPlayersOnTeam(team.getTeamKey()).stream()
                .filter(p -> !Boolean.TRUE.equals(p.getTag(BedWarsGame.ELIMINATED_TAG)))
                .count();

            teamStates.add(new RecordableScoreboardState.TeamScoreboardState(
                team.getTeamKey().name(),
                team.isBedAlive(),
                alivePlayers
            ));
        }

        recorder.record(new RecordableScoreboardState(nextEventName, nextEventSeconds, teamStates));
    }

    public static byte[] serializeItemStack(ItemStack itemStack) {
        try {
            CompoundBinaryTag nbt = itemStack.toItemNBT();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BinaryTagIO.writer().writeNameless(nbt, out);

            return out.toByteArray();
        } catch (Exception e) {
            Logger.error(e, "Failed to serialize item stack");
            return new byte[0];
        }
    }

    public BlockChangeDispatcher getBlockChangeDispatcher() {
        return dispatchers.getDispatcher(BlockChangeDispatcher.class);
    }

    public EntityLifecycleDispatcher getEntityLifecycleDispatcher() {
        return dispatchers.getDispatcher(EntityLifecycleDispatcher.class);
    }

    private static final int MAP_CHUNK_RADIUS = 8; // this is a max limit.


    private String serializeAndUploadMap(InstanceContainer instance, String mapName, int centerChunkX, int centerChunkZ) {
        try {
            MapSerializer.SerializedMap serializedMap = MapSerializer.serializeRegion(
                instance, centerChunkX, centerChunkZ, MAP_CHUNK_RADIUS
            );

            String mapHash = serializedMap.hash();

            CompletableFuture.runAsync(() -> {
                try {
                    var uploadMsg = new net.swofty.commons.protocol.objects.replay.ReplayMapUploadProtocolObject.MapUploadMessage(
                        mapHash, mapName, serializedMap.compressedData()
                    );
                    sendToService(uploadMsg);

                    Logger.info("Map {} uploaded: {} -> {} bytes ({}% compression)",
                        mapName, serializedMap.uncompressedSize(), serializedMap.compressedSize(),
                        100 - (serializedMap.compressedSize() * 100 / Math.max(1, serializedMap.uncompressedSize())));
                } catch (Exception e) {
                    Logger.error(e, "Failed to upload map {}", mapName);
                }
            });

            return mapHash;

        } catch (Exception e) {
            Logger.error(e, "Failed to serialize map {}", mapName);
            return mapName.toLowerCase().replace(" ", "_");
        }
    }
}
