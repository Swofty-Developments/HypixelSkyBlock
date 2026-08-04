package net.swofty.type.replayviewer.event;

import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.replay.ReplayLoadProtocolObject;
import net.swofty.commons.protocol.objects.replay.ReplayMapLoadProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.game.replay.ReplayError;
import net.swofty.type.game.replay.ReplayMetadata;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.ScheduleUtility;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.playback.MapDeserializer;
import net.swofty.type.replayviewer.playback.ReplayData;
import net.swofty.type.replayviewer.playback.ReplaySession;
import net.swofty.type.replayviewer.redis.service.TypedViewReplayHandler;
import net.swofty.type.replayviewer.util.ReplayShareCodec;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerJoinEvent implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.ALL, phase = EventPhase.CONNECT)
    public void run(AsyncPlayerConfigurationEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        tryGame(player, false, event);
    }

    private void tryGame(HypixelPlayer player, boolean isRetry, AsyncPlayerConfigurationEvent event) {
        String replayStr = TypedViewReplayHandler.replay.remove(player.getUuid());
        if (replayStr == null) {
            if (!isRetry) {
                ScheduleUtility.delay(() -> tryGame(player, true, event), 20);
                return;
            }
            event.setSpawningInstance(HypixelConst.getEmptyInstance());
            player.sendTo(ServerType.PROTOTYPE_LOBBY);
            return;
        }

        UUID replayId;
        try {
            replayId = UUID.fromString(replayStr);
        } catch (IllegalArgumentException e) {
            event.setSpawningInstance(HypixelConst.getEmptyInstance());
            player.sendTo(ServerType.PROTOTYPE_LOBBY);
            return;
        }

        var existingSession = TypeReplayViewerLoader.getSessionByReplayId(replayId);
        if (existingSession.isPresent()) {
            ReplaySession session = existingSession.get();
            event.setSpawningInstance(session.getInstance());

            Pos spawnPos = new Pos(session.getMetadata().getMapCenterX(), 100, session.getMetadata().getMapCenterZ());
            event.getPlayer().setRespawnPoint(spawnPos);

            TypeReplayViewerLoader.registerSession(player.getUuid(), session);
            player.setRespawnPoint(spawnPos);
            return;
        }

        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);

        event.setSpawningInstance(instance);
        event.getPlayer().setRespawnPoint(new Pos(0, 100, 0));

        CompletableFuture.runAsync(() -> loadReplay(player, replayId, instance));
    }

    private void loadReplay(Player player, UUID replayId, InstanceContainer instance) {
        try {
            // Get share code if present
            String shareCode = TypedViewReplayHandler.getAndRemoveShareCode(player.getUuid());

            ProxyService replayService = new ProxyService(ServiceType.REPLAY);
            var request = new ReplayLoadProtocolObject.LoadRequest(replayId);

            ReplayLoadProtocolObject.LoadResponse response = replayService
                .<ReplayLoadProtocolObject.LoadRequest, ReplayLoadProtocolObject.LoadResponse>handleRequest(request)
                .join();

            Logger.info("shit is happening");
            if (!response.success()) {
                Logger.error("Response failed: " + response.errorMessage());
                return;
            }

            Logger.info("shit is happening");
            if (response.metadata() == null) {
                Logger.error("Response is missing metadata.");
                return;
            }

            Logger.info("shit is happening");
            ReplayLoadProtocolObject.ReplayMetadata protoMetadata = response.metadata();
            Map<String, ReplayMetadata.TeamInfo> teamInfo = new HashMap<>();
            protoMetadata.teamInfo().forEach((teamId, info) ->
                teamInfo.put(teamId, new ReplayMetadata.TeamInfo(info.name(), info.colorCode(), info.color())));

            ReplayMetadata metadata = ReplayMetadata.builder()
                .replayId(protoMetadata.replayId())
                .gameId(protoMetadata.gameId())
                .serverType(protoMetadata.serverType())
                .serverId(protoMetadata.serverId())
                .gameTypeName(protoMetadata.gameTypeName())
                .mapName(protoMetadata.mapName())
                .mapHash(protoMetadata.mapHash())
                .version(protoMetadata.version())
                .startTime(protoMetadata.startTime())
                .endTime(protoMetadata.endTime())
                .durationTicks(protoMetadata.durationTicks())
                .players(protoMetadata.players())
                .teams(protoMetadata.teams())
                .teamInfo(teamInfo)
                .winnerId(protoMetadata.winnerId())
                .dataSize(protoMetadata.dataSize())
                .mapCenterX(protoMetadata.mapCenterX())
                .mapCenterZ(protoMetadata.mapCenterZ())
                .build();

            Logger.info("shit is happening");
            ReplayData replayData = new ReplayData();
            if (response.dataChunks() != null && !response.dataChunks().isEmpty()) {
                ReplayData.IntegrityReport integrityReport = replayData.loadFromProtocolChunks(
                    response.dataChunks(),
                    metadata.getDurationTicks()
                );

                if (integrityReport.hasIssues()) {
                    player.sendMessage("§cSome moments may be missing. " + ReplayError.REPLAY_INCOMPLETE.format());
                }
            }
            Logger.info("shit is happening");

            // Load map data
            loadMapData(metadata.getMapHash(), instance, player);

            // Determine spawn position - use share code if available
            Pos spawnPos;
            int startTick = 0;

            if (shareCode != null) {
                ReplayShareCodec.ShareData shareData = ReplayShareCodec.decode(
                    shareCode,
                    metadata.getMapCenterX(),
                    metadata.getMapCenterZ()
                );
                if (shareData != null) {
                    spawnPos = shareData.position();
                    startTick = Math.min(shareData.tick(), metadata.getDurationTicks() - 1);
                    player.sendMessage("§aRestored shared replay position");
                } else {
                    spawnPos = new Pos(metadata.getMapCenterX(), 100, metadata.getMapCenterZ());
                    player.sendMessage("§eInvalid share code, using default position");
                }
            } else {
                spawnPos = new Pos(metadata.getMapCenterX(), 100, metadata.getMapCenterZ());
            }

            Logger.info("teleportingplayer");
            player.teleport(spawnPos);

            Logger.info("opening session");
            ReplaySession session = new ReplaySession(metadata, instance, replayData);
            Logger.info("adding viewer");
            session.addViewer(player);
            Logger.info("registering session");
            TypeReplayViewerLoader.registerSession(player.getUuid(), session);

            if (startTick > 0) {
                session.seekTo(startTick);
            }

            Logger.info("playing session?");
            session.play();
        } catch (Exception e) {
            Logger.error(e, "Failed to load replay {}", replayId);
        }
    }

    private void loadMapData(String mapHash, InstanceContainer instance, Player player) {
        if (mapHash == null || mapHash.isEmpty()) {
            Logger.warn("No map hash provided, skipping map load");
            return;
        }

        try {
            Logger.info("request init");
            ProxyService replayService = new ProxyService(ServiceType.REPLAY);
            var request = new ReplayMapLoadProtocolObject.MapLoadRequest(mapHash);

            ReplayMapLoadProtocolObject.MapLoadResponse response = replayService
                .<ReplayMapLoadProtocolObject.MapLoadRequest, ReplayMapLoadProtocolObject.MapLoadResponse>handleRequest(request)
                .join();

            Logger.info("request sent");

            if (!response.success() || !response.found()) {
                Logger.warn("Map {} not found in replay service", mapHash);
                player.sendMessage("§eMap data not available, using empty world.");
                return;
            }

            if (response.compressedData() == null || response.compressedData().length == 0) {
                Logger.warn("Map {} has no data", mapHash);
                return;
            }

            // Deserialize and apply map
            MapDeserializer.loadMap(instance, response.compressedData())
                    .whenComplete((ignored, throwable) -> {
                        if (throwable != null) {
                            Logger.error(throwable, "Failed to load map");
                            return;
                        }

                        Logger.info("Loaded map {} ({} bytes)", mapHash, response.compressedData().length);
                    });
        } catch (Exception e) {
            Logger.error(e, "Failed to load map {}", mapHash);
            player.sendMessage("§eFailed to load map: " + e.getMessage());
        }
    }
}
