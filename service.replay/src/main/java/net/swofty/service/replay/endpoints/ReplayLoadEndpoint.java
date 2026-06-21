package net.swofty.service.replay.endpoints;

import net.swofty.commons.ServerType;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.replay.ReplayLoadProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.replay.ReplayService;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReplayLoadEndpoint implements ServiceEndpoint<
    ReplayLoadProtocolObject.LoadRequest,
    ReplayLoadProtocolObject.LoadResponse> {

    @Override
    public ReplayLoadProtocolObject associatedProtocolObject() {
        return new ReplayLoadProtocolObject();
    }

    @Override
    public ReplayLoadProtocolObject.LoadResponse onMessage(
        ServiceProxyRequest message,
        ReplayLoadProtocolObject.LoadRequest msg) {

        try {
            UUID replayId = msg.replayId();
            Document metadataDoc = ReplayService.getDatabase().getReplayMetadata(replayId);
            if (metadataDoc == null) {
                Logger.warn("Replay not found: {}", replayId);
                return new ReplayLoadProtocolObject.LoadResponse(
                    false,
                    "Replay not found",
                    null,
                    null
                );
            }

            // Parse metadata
            ReplayLoadProtocolObject.ReplayMetadata metadata = parseMetadata(metadataDoc);

            // Fetch data chunks
            List<Document> chunkDocs = ReplayService.getDatabase().getReplayDataChunks(replayId);
            List<ReplayLoadProtocolObject.DataChunk> dataChunks = new ArrayList<>();

            for (Document chunkDoc : chunkDocs) {
                byte[] data = chunkDoc.get("data", org.bson.types.Binary.class).getData();
                dataChunks.add(new ReplayLoadProtocolObject.DataChunk(
                    chunkDoc.getInteger("chunkIndex"),
                    chunkDoc.getInteger("startTick"),
                    chunkDoc.getInteger("endTick"),
                    data
                ));
            }

            Logger.debug("Loaded replay {} with {} chunks", replayId, dataChunks.size());

            return new ReplayLoadProtocolObject.LoadResponse(
                true,
                null,
                metadata,
                dataChunks
            );

        } catch (Exception e) {
            Logger.error(e, "Failed to load replay {}", msg.replayId());
            return new ReplayLoadProtocolObject.LoadResponse(
                false,
                "Failed to load replay: " + e.getMessage(),
                null,
                null
            );
        }
    }

    private ReplayLoadProtocolObject.ReplayMetadata parseMetadata(Document doc) {
        Map<UUID, String> players = new HashMap<>();
        Document playerNames = doc.get("playerNames", Document.class);
        if (playerNames != null) {
            playerNames.forEach((key, value) -> players.put(UUID.fromString(key), (String) value));
        }

        Map<String, List<UUID>> teams = new HashMap<>();
        Document teamsDoc = doc.get("teams", Document.class);
        if (teamsDoc != null) {
            teamsDoc.forEach((teamId, value) -> {
                @SuppressWarnings("unchecked")
                List<String> playerList = (List<String>) value;
                List<UUID> playerUUIDs = new ArrayList<>();
                for (String uuidStr : playerList) {
                    playerUUIDs.add(UUID.fromString(uuidStr));
                }
                teams.put(teamId, playerUUIDs);
            });
        }

        Map<String, ReplayLoadProtocolObject.TeamInfo> teamInfo = new HashMap<>();
        Document teamInfoDoc = doc.get("teamInfo", Document.class);
        if (teamInfoDoc != null) {
            teamInfoDoc.forEach((teamId, value) -> {
                Document infoDoc = (Document) value;
                teamInfo.put(teamId, new ReplayLoadProtocolObject.TeamInfo(
                    infoDoc.getString("name"),
                    infoDoc.getString("colorCode"),
                    infoDoc.getInteger("color")
                ));
            });
        }

        String mapHash = doc.getString("mapHash");
        String winnerId = doc.getString("winnerId");
        String winnerType = doc.getString("winnerType");

        return new ReplayLoadProtocolObject.ReplayMetadata(
            UUID.fromString(doc.getString("replayId")),
            doc.getString("gameId"),
            ServerType.valueOf(doc.getString("serverType")),
            doc.getString("serverId"),
            doc.getString("gameTypeName"),
            doc.getString("mapName"),
            mapHash,
            doc.getInteger("version", 1),
            doc.getLong("startTime"),
            doc.getLong("endTime"),
            doc.getInteger("durationTicks"),
            players,
            teams,
            teamInfo,
            winnerId,
            winnerType,
            doc.getLong("dataSize"),
            doc.getDouble("mapCenterX"),
            doc.getDouble("mapCenterZ")
        );
    }
}
