package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.game.game.GameObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameHeartbeatProtocolObject extends ProtocolObject
    <GameHeartbeatProtocolObject.HeartbeatMessage,
        GameHeartbeatProtocolObject.HeartbeatResponse> {

    @Override
    public Serializer<HeartbeatMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(HeartbeatMessage value) {
                JSONObject json = new JSONObject();
                json.put("uuid", value.uuid().toString());
                json.put("shortName", value.shortName());
                json.put("type", value.type().name());
                json.put("maxPlayers", value.maxPlayers());
                json.put("onlinePlayers", value.onlinePlayers());
                JSONArray games = new JSONArray();
                for (GameObject game : value.games()) {
                    games.put(game.toJSON());
                }
                json.put("games", games);

                if (!value.mapAdvertisements().isEmpty()) {
                    JSONArray maps = new JSONArray();
                    for (MapAdvertisement advertisement : value.mapAdvertisements()) {
                        JSONObject map = new JSONObject();
                        map.put("mapId", advertisement.mapId());
                        map.put("mapName", advertisement.mapName());
                        map.put("supportedModes", new JSONArray(advertisement.supportedModes()));
                        maps.put(map);
                    }
                    json.put("mapAdvertisements", maps);
                }

                if (value.remainingGameSlots() != null) {
                    json.put("remainingGameSlots", value.remainingGameSlots());
                }
                return json.toString();
            }

            @Override
            public HeartbeatMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                UUID uuid = UUID.fromString(obj.getString("uuid"));
                String shortName = obj.getString("shortName");
                ServerType type = ServerType.valueOf(obj.getString("type"));
                List<GameObject> games = new ArrayList<>();
                JSONArray gamesArray = obj.getJSONArray("games");
                for (int i = 0; i < gamesArray.length(); i++) {
                    JSONObject game = gamesArray.getJSONObject(i);
                    games.add(GameObject.fromJSON(game));
                }
                int max = obj.getInt("maxPlayers");
                int online = obj.getInt("onlinePlayers");

                List<MapAdvertisement> mapAdvertisements = new ArrayList<>();
                if (obj.has("mapAdvertisements")) {
                    JSONArray mapsArray = obj.getJSONArray("mapAdvertisements");
                    for (int i = 0; i < mapsArray.length(); i++) {
                        JSONObject map = mapsArray.getJSONObject(i);
                        JSONArray supportedModesArray = map.optJSONArray("supportedModes");
                        List<String> supportedModes = new ArrayList<>();
                        if (supportedModesArray != null) {
                            for (int j = 0; j < supportedModesArray.length(); j++) {
                                supportedModes.add(supportedModesArray.getString(j));
                            }
                        }
                        mapAdvertisements.add(new MapAdvertisement(
                            map.optString("mapId", map.getString("mapName")),
                            map.getString("mapName"),
                            supportedModes
                        ));
                    }
                }

                Integer remainingGameSlots = obj.has("remainingGameSlots") ? obj.getInt("remainingGameSlots") : null;
                return new HeartbeatMessage(uuid, shortName, type, max, online, games, mapAdvertisements, remainingGameSlots);
            }

            @Override
            public HeartbeatMessage clone(HeartbeatMessage value) {
                return new HeartbeatMessage(
                    value.uuid(),
                    value.shortName(),
                    value.type(),
                    value.maxPlayers(),
                    value.onlinePlayers(),
                    value.games(),
                    value.mapAdvertisements(),
                    value.remainingGameSlots()
                );
            }
        };
    }

    @Override
    public Serializer<HeartbeatResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(HeartbeatResponse value) {
                JSONObject json = new JSONObject();
                json.put("ok", value.ok);
                return json.toString();
            }

            @Override
            public HeartbeatResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new HeartbeatResponse(obj.getBoolean("ok"));
            }

            @Override
            public HeartbeatResponse clone(HeartbeatResponse value) {
                return new HeartbeatResponse(value.ok);
            }
        };
    }

    public record HeartbeatMessage(
        UUID uuid,
        String shortName,
        ServerType type,
        int maxPlayers,
        int onlinePlayers,
        List<GameObject> games,
        List<MapAdvertisement> mapAdvertisements,
        Integer remainingGameSlots
    ) {
        public HeartbeatMessage {
            Objects.requireNonNull(uuid, "uuid");
            Objects.requireNonNull(shortName, "shortName");
            Objects.requireNonNull(type, "type");
            games = games != null ? List.copyOf(games) : List.of();
            mapAdvertisements = mapAdvertisements != null ? List.copyOf(mapAdvertisements) : List.of();
        }

        public HeartbeatMessage(UUID uuid, String shortName, ServerType type, int maxPlayers, int onlinePlayers, List<GameObject> games) {
            this(uuid, shortName, type, maxPlayers, onlinePlayers, games, List.of(), null);
        }
    }

    public record MapAdvertisement(String mapId, String mapName, List<String> supportedModes) {
        public MapAdvertisement {
            Objects.requireNonNull(mapId, "mapId");
            Objects.requireNonNull(mapName, "mapName");
            supportedModes = supportedModes != null ? List.copyOf(supportedModes) : List.of();
        }
    }

    public record HeartbeatResponse(boolean ok) {
    }
}
