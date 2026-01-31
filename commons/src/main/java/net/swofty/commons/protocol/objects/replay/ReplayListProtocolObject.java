package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReplayListProtocolObject extends ProtocolObject<
        ReplayListProtocolObject.ListRequest,
        ReplayListProtocolObject.ListResponse> {

    @Override
    public Serializer<ListRequest> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(ListRequest value) {
                JSONObject json = new JSONObject();
                json.put("playerId", value.playerId.toString());
                json.put("limit", value.limit);
                if (value.filterType != null) {
                    json.put("filterType", value.filterType.name());
                }
                return json.toString();
            }

            @Override
            public ListRequest deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                ServerType filterType = obj.has("filterType") ?
                        ServerType.valueOf(obj.getString("filterType")) : null;
                return new ListRequest(
                        UUID.fromString(obj.getString("playerId")),
                        obj.getInt("limit"),
                        filterType
                );
            }

            @Override
            public ListRequest clone(ListRequest value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<ListResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(ListResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                JSONArray replays = new JSONArray();
                for (ReplaySummary summary : value.replays) {
                    JSONObject s = new JSONObject();
                    s.put("replayId", summary.replayId.toString());
                    s.put("gameId", summary.gameId);
                    s.put("serverType", summary.serverType.name());
                    s.put("gameTypeName", summary.gameTypeName);
                    s.put("mapName", summary.mapName);
                    s.put("startTime", summary.startTime);
                    s.put("endTime", summary.endTime);
                    s.put("durationTicks", summary.durationTicks);
                    s.put("playerCount", summary.playerCount);
                    s.put("winnerId", summary.winnerId != null ? summary.winnerId : "");
                    s.put("winnerType", summary.winnerType != null ? summary.winnerType : "");
                    s.put("dataSize", summary.dataSize);

                    JSONObject players = new JSONObject();
                    summary.players.forEach((uuid, name) -> players.put(uuid.toString(), name));
                    s.put("players", players);

                    replays.put(s);
                }
                json.put("replays", replays);
                return json.toString();
            }

            @Override
            public ListResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                List<ReplaySummary> replays = new ArrayList<>();
                JSONArray arr = obj.getJSONArray("replays");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject s = arr.getJSONObject(i);
                    Map<UUID, String> players = new HashMap<>();
                    JSONObject playersObj = s.getJSONObject("players");
                    playersObj.keySet().forEach(key ->
                            players.put(UUID.fromString(key), playersObj.getString(key)));

                    String winnerId = s.optString("winnerId", null);
                    String winnerType = s.optString("winnerType", null);

                    replays.add(new ReplaySummary(
                            UUID.fromString(s.getString("replayId")),
                            s.getString("gameId"),
                            ServerType.valueOf(s.getString("serverType")),
                            s.getString("gameTypeName"),
                            s.getString("mapName"),
                            s.getLong("startTime"),
                            s.getLong("endTime"),
                            s.getInt("durationTicks"),
                            s.getInt("playerCount"),
                            players,
                            winnerId != null && !winnerId.isEmpty() ? winnerId : null,
                            winnerType != null && !winnerType.isEmpty() ? winnerType : null,
                            s.getLong("dataSize")
                    ));
                }
                return new ListResponse(obj.getBoolean("success"), replays);
            }

            @Override
            public ListResponse clone(ListResponse value) {
                return value;
            }
        };
    }

    public record ListRequest(UUID playerId, int limit, ServerType filterType) {}

    public record ListResponse(boolean success, List<ReplaySummary> replays) {}

    public record ReplaySummary(
            UUID replayId,
            String gameId,
            ServerType serverType,
            String gameTypeName,
            String mapName,
            long startTime,
            long endTime,
            int durationTicks,
            int playerCount,
            Map<UUID, String> players,
            String winnerId,
            String winnerType,
            long dataSize
    ) {}
}
