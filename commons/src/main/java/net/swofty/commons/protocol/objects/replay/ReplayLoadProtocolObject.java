package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReplayLoadProtocolObject extends ProtocolObject<
        ReplayLoadProtocolObject.LoadRequest,
        ReplayLoadProtocolObject.LoadResponse> {

    @Override
    public Serializer<LoadRequest> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(LoadRequest value) {
                JSONObject json = new JSONObject();
                json.put("replayId", value.replayId.toString());
                return json.toString();
            }

            @Override
            public LoadRequest deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new LoadRequest(
                        UUID.fromString(obj.getString("replayId"))
                );
            }

            @Override
            public LoadRequest clone(LoadRequest value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<LoadResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(LoadResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("errorMessage", value.errorMessage != null ? value.errorMessage : "");

                if (value.metadata != null) {
                    JSONObject metadata = new JSONObject();
                    metadata.put("replayId", value.metadata.replayId.toString());
                    metadata.put("gameId", value.metadata.gameId);
                    metadata.put("serverType", value.metadata.serverType.name());
                    metadata.put("serverId", value.metadata.serverId);
                    metadata.put("gameTypeName", value.metadata.gameTypeName);
                    metadata.put("mapName", value.metadata.mapName);
                    metadata.put("mapHash", value.metadata.mapHash != null ? value.metadata.mapHash : "");
                    metadata.put("version", value.metadata.version);
                    metadata.put("startTime", value.metadata.startTime);
                    metadata.put("endTime", value.metadata.endTime);
                    metadata.put("durationTicks", value.metadata.durationTicks);
                    metadata.put("mapCenterX", value.metadata.mapCenterX);
                    metadata.put("mapCenterZ", value.metadata.mapCenterZ);
                    metadata.put("dataSize", value.metadata.dataSize);
                    metadata.put("winnerId", value.metadata.winnerId != null ? value.metadata.winnerId : "");
                    metadata.put("winnerType", value.metadata.winnerType != null ? value.metadata.winnerType : "");

                    JSONObject players = new JSONObject();
                    value.metadata.players.forEach((uuid, name) -> players.put(uuid.toString(), name));
                    metadata.put("players", players);

                    JSONObject teams = new JSONObject();
                    value.metadata.teams.forEach((teamId, playerList) -> {
                        JSONArray playerArray = new JSONArray();
                        playerList.forEach(uuid -> playerArray.put(uuid.toString()));
                        teams.put(teamId, playerArray);
                    });
                    metadata.put("teams", teams);

                    JSONObject teamInfo = new JSONObject();
                    value.metadata.teamInfo.forEach((teamId, info) -> {
                        JSONObject infoObj = new JSONObject();
                        infoObj.put("name", info.name);
                        infoObj.put("colorCode", info.colorCode);
                        infoObj.put("color", info.color);
                        teamInfo.put(teamId, infoObj);
                    });
                    metadata.put("teamInfo", teamInfo);

                    json.put("metadata", metadata);
                }

                if (value.dataChunks != null) {
                    JSONArray chunks = new JSONArray();
                    for (DataChunk chunk : value.dataChunks) {
                        JSONObject chunkObj = new JSONObject();
                        chunkObj.put("chunkIndex", chunk.chunkIndex);
                        chunkObj.put("startTick", chunk.startTick);
                        chunkObj.put("endTick", chunk.endTick);
                        chunkObj.put("data", Base64.getEncoder().encodeToString(chunk.data));
                        chunks.put(chunkObj);
                    }
                    json.put("dataChunks", chunks);
                }

                return json.toString();
            }

            @Override
            public LoadResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                boolean success = obj.getBoolean("success");
                String errorMessage = obj.optString("errorMessage", null);
                if (errorMessage != null && errorMessage.isEmpty()) {
                    errorMessage = null;
                }

                ReplayMetadata metadata = null;
                if (obj.has("metadata") && !obj.isNull("metadata")) {
                    JSONObject metaObj = obj.getJSONObject("metadata");

                    Map<UUID, String> players = new HashMap<>();
                    JSONObject playersObj = metaObj.getJSONObject("players");
                    playersObj.keySet().forEach(key ->
                            players.put(UUID.fromString(key), playersObj.getString(key)));

                    Map<String, List<UUID>> teams = new HashMap<>();
                    JSONObject teamsObj = metaObj.getJSONObject("teams");
                    teamsObj.keySet().forEach(teamId -> {
                        JSONArray playerArray = teamsObj.getJSONArray(teamId);
                        List<UUID> playerList = new ArrayList<>();
                        for (int i = 0; i < playerArray.length(); i++) {
                            playerList.add(UUID.fromString(playerArray.getString(i)));
                        }
                        teams.put(teamId, playerList);
                    });

                    Map<String, TeamInfo> teamInfo = new HashMap<>();
                    JSONObject teamInfoObj = metaObj.getJSONObject("teamInfo");
                    teamInfoObj.keySet().forEach(teamId -> {
                        JSONObject infoObj = teamInfoObj.getJSONObject(teamId);
                        teamInfo.put(teamId, new TeamInfo(
                                infoObj.getString("name"),
                                infoObj.getString("colorCode"),
                                infoObj.getInt("color")
                        ));
                    });

                    String winnerId = metaObj.optString("winnerId", null);
                    String winnerType = metaObj.optString("winnerType", null);
                    String mapHash = metaObj.optString("mapHash", null);

                    metadata = new ReplayMetadata(
                            UUID.fromString(metaObj.getString("replayId")),
                            metaObj.getString("gameId"),
                            ServerType.valueOf(metaObj.getString("serverType")),
                            metaObj.getString("serverId"),
                            metaObj.getString("gameTypeName"),
                            metaObj.getString("mapName"),
                            mapHash != null && !mapHash.isEmpty() ? mapHash : null,
                            metaObj.getInt("version"),
                            metaObj.getLong("startTime"),
                            metaObj.getLong("endTime"),
                            metaObj.getInt("durationTicks"),
                            players,
                            teams,
                            teamInfo,
                            winnerId != null && !winnerId.isEmpty() ? winnerId : null,
                            winnerType != null && !winnerType.isEmpty() ? winnerType : null,
                            metaObj.getLong("dataSize"),
                            metaObj.getDouble("mapCenterX"),
                            metaObj.getDouble("mapCenterZ")
                    );
                }

                List<DataChunk> dataChunks = new ArrayList<>();
                if (obj.has("dataChunks") && !obj.isNull("dataChunks")) {
                    JSONArray chunksArray = obj.getJSONArray("dataChunks");
                    for (int i = 0; i < chunksArray.length(); i++) {
                        JSONObject chunkObj = chunksArray.getJSONObject(i);
                        dataChunks.add(new DataChunk(
                                chunkObj.getInt("chunkIndex"),
                                chunkObj.getInt("startTick"),
                                chunkObj.getInt("endTick"),
                                Base64.getDecoder().decode(chunkObj.getString("data"))
                        ));
                    }
                }

                return new LoadResponse(success, errorMessage, metadata, dataChunks);
            }

            @Override
            public LoadResponse clone(LoadResponse value) {
                return value;
            }
        };
    }

    public record LoadRequest(UUID replayId) {}

    public record LoadResponse(
            boolean success,
            String errorMessage,
            ReplayMetadata metadata,
            List<DataChunk> dataChunks
    ) {}

    public record ReplayMetadata(
            UUID replayId,
            String gameId,
            ServerType serverType,
            String serverId,
            String gameTypeName,
            String mapName,
            String mapHash,
            int version,
            long startTime,
            long endTime,
            int durationTicks,
            Map<UUID, String> players,
            Map<String, List<UUID>> teams,
            Map<String, TeamInfo> teamInfo,
            String winnerId,
            String winnerType,
            long dataSize,
            double mapCenterX,
            double mapCenterZ
    ) {}

    public record TeamInfo(
            String name,
            String colorCode,
            int color
    ) {}

    public record DataChunk(
            int chunkIndex,
            int startTick,
            int endTick,
            byte[] data
    ) {}
}
