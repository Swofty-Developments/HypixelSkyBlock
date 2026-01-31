package net.swofty.type.replayviewer.event;

import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.replay.ReplayLoadProtocolObject;
import net.swofty.commons.replay.ReplayMetadata;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.commands.replay.ReplaysCommand;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.playback.ReplayData;
import net.swofty.type.replayviewer.playback.ReplaySession;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerJoinEvent implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        Player player = event.getPlayer();

        // TODO: utter slop
        UUID replayId = ReplaysCommand.getAndRemovePendingReplayId(player.getUuid());

        if (replayId == null) {
            player.sendMessage("§cNo replay specified. Use /replays to browse available replays.");
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
            player.sendMessage("§7Fetching replay data...");

            // Fetch replay data from the replay service
            ProxyService replayService = new ProxyService(ServiceType.REPLAY);
            var request = new ReplayLoadProtocolObject.LoadRequest(replayId);

            ReplayLoadProtocolObject.LoadResponse response = replayService
                    .<ReplayLoadProtocolObject.LoadRequest, ReplayLoadProtocolObject.LoadResponse>handleRequest(request)
                    .join();

            if (!response.success()) {
                player.sendMessage("§cFailed to load replay: " +
                        (response.errorMessage() != null ? response.errorMessage() : "Unknown error"));
                return;
            }

            if (response.metadata() == null) {
                player.sendMessage("§cReplay metadata is missing.");
                return;
            }

            // Convert protocol metadata to commons ReplayMetadata
            ReplayLoadProtocolObject.ReplayMetadata protoMetadata = response.metadata();
            Map<String, ReplayMetadata.TeamInfo> teamInfo = new HashMap<>();
            protoMetadata.teamInfo().forEach((teamId, info) ->
                    teamInfo.put(teamId, new ReplayMetadata.TeamInfo(info.name(), info.colorCode(), info.color())));

            ReplayMetadata metadata = ReplayMetadata.builder()
                    .replayId(protoMetadata.replayId())
                    .gameId(protoMetadata.gameId())
                    .serverType(protoMetadata.serverType())
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

            // Load replay data chunks
            ReplayData replayData = new ReplayData();
            if (response.dataChunks() != null && !response.dataChunks().isEmpty()) {
                List<byte[]> chunks = response.dataChunks().stream()
                        .map(ReplayLoadProtocolObject.DataChunk::data)
                        .toList();
                replayData.loadFromChunks(chunks);
            }

            // Create session
            ReplaySession session = new ReplaySession(player, metadata, instance, replayData);
            TypeReplayViewerLoader.registerSession(player.getUuid(), session);

            player.sendMessage("§aReplay loaded! Use §e/replay play §ato start.");
            player.sendMessage("§7Commands: /replay play|pause|speed|skip|goto|info|leave");

            // Auto-start playback
            session.play();

        } catch (Exception e) {
            Logger.error(e, "Failed to load replay {}", replayId);
            player.sendMessage("§cFailed to load replay: " + e.getMessage());
        }
    }
}
