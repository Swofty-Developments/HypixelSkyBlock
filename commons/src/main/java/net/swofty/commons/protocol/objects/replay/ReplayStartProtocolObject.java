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

public class ReplayStartProtocolObject extends ProtocolObject<
        ReplayStartProtocolObject.StartMessage,
        ReplayStartProtocolObject.StartResponse> {

    @Override
    public Serializer<StartMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(StartMessage value) {
                JSONObject json = new JSONObject();
                json.put("replayId", value.replayId.toString());
                json.put("gameId", value.gameId);
                json.put("serverType", value.serverType.name());
                json.put("gameTypeName", value.gameTypeName);
                json.put("mapName", value.mapName);
                json.put("mapHash", value.mapHash);
                json.put("startTime", value.startTime);
                json.put("mapCenterX", value.mapCenterX);
                json.put("mapCenterZ", value.mapCenterZ);

                JSONObject players = new JSONObject();
                value.players.forEach((uuid, name) -> players.put(uuid.toString(), name));
                json.put("players", players);

                JSONObject teams = new JSONObject();
                value.teams.forEach((teamId, playerUuids) -> {
                    JSONArray arr = new JSONArray();
                    playerUuids.forEach(uuid -> arr.put(uuid.toString()));
                    teams.put(teamId, arr);
                });
                json.put("teams", teams);

                JSONObject teamInfo = new JSONObject();
                value.teamInfo.forEach((teamId, info) -> {
                    JSONObject infoJson = new JSONObject();
                    infoJson.put("name", info.name);
                    infoJson.put("colorCode", info.colorCode);
                    infoJson.put("color", info.color);
                    teamInfo.put(teamId, infoJson);
                });
                json.put("teamInfo", teamInfo);

                return json.toString();
            }

            @Override
            public StartMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);

                Map<UUID, String> players = new HashMap<>();
                JSONObject playersObj = obj.getJSONObject("players");
                playersObj.keySet().forEach(key -> players.put(UUID.fromString(key), playersObj.getString(key)));

                Map<String, List<UUID>> teams = new HashMap<>();
                JSONObject teamsObj = obj.getJSONObject("teams");
                teamsObj.keySet().forEach(teamId -> {
                    List<UUID> uuids = new ArrayList<>();
                    JSONArray arr = teamsObj.getJSONArray(teamId);
                    for (int i = 0; i < arr.length(); i++) {
                        uuids.add(UUID.fromString(arr.getString(i)));
                    }
                    teams.put(teamId, uuids);
                });

                Map<String, TeamInfo> teamInfo = new HashMap<>();
                JSONObject teamInfoObj = obj.getJSONObject("teamInfo");
                teamInfoObj.keySet().forEach(teamId -> {
                    JSONObject info = teamInfoObj.getJSONObject(teamId);
                    teamInfo.put(teamId, new TeamInfo(
                            info.getString("name"),
                            info.getString("colorCode"),
                            info.getInt("color")
                    ));
                });

                return new StartMessage(
                        UUID.fromString(obj.getString("replayId")),
                        obj.getString("gameId"),
                        ServerType.valueOf(obj.getString("serverType")),
                        obj.getString("gameTypeName"),
                        obj.getString("mapName"),
                        obj.getString("mapHash"),
                        obj.getLong("startTime"),
                        obj.getDouble("mapCenterX"),
                        obj.getDouble("mapCenterZ"),
                        players,
                        teams,
                        teamInfo
                );
            }

            @Override
            public StartMessage clone(StartMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<StartResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(StartResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("message", value.message);
                return json.toString();
            }

            @Override
            public StartResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new StartResponse(obj.getBoolean("success"), obj.optString("message", null));
            }

            @Override
            public StartResponse clone(StartResponse value) {
                return value;
            }
        };
    }

    public record StartMessage(
            UUID replayId,
            String gameId,
            ServerType serverType,
            String gameTypeName,
            String mapName,
            String mapHash,
            long startTime,
            double mapCenterX,
            double mapCenterZ,
            Map<UUID, String> players,
            Map<String, List<UUID>> teams,
            Map<String, TeamInfo> teamInfo
    ) {}

    public record StartResponse(boolean success, String message) {}

    public record TeamInfo(String name, String colorCode, int color) {}
}
