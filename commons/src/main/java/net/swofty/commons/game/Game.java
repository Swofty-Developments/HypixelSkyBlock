package net.swofty.commons.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.ServerType;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private List<UUID> involvedPlayers;
    private List<UUID> disconnectedPlayers;
    private UUID gameId;
    private ServerType type;
    private String map;
    private String gameTypeName; // e.g., "CLASSIC", "SOLO", "DOUBLE_UP"

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("gameId", gameId.toString());
        json.put("type", type.name());
        json.put("map", map);
        json.put("gameTypeName", gameTypeName != null ? gameTypeName : "");
        List<String> players = involvedPlayers.stream().map(UUID::toString).toList();
        json.put("players", players);
        List<String> disconnected = disconnectedPlayers != null
                ? disconnectedPlayers.stream().map(UUID::toString).toList()
                : List.of();
        json.put("disconnectedPlayers", disconnected);
        return json;
    }

    public static Game fromJSON(JSONObject json) {
        Game game = new Game();
        game.gameId = UUID.fromString(json.getString("gameId"));
        game.type = ServerType.valueOf(json.getString("type"));
        game.map = json.getString("map");
        game.gameTypeName = json.optString("gameTypeName", null);
        game.involvedPlayers = json.getJSONArray("players").toList().stream().map(obj -> UUID.fromString(obj.toString())).toList();
        game.disconnectedPlayers = json.has("disconnectedPlayers")
                ? json.getJSONArray("disconnectedPlayers").toList().stream().map(obj -> UUID.fromString(obj.toString())).toList()
                : List.of();
        return game;
    }
}
