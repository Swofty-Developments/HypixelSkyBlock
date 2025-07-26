package net.swofty.commons;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UnderstandableProxyServer(String name, UUID uuid, ServerType type, int port, List<UUID> players) {
    public JSONObject toJSON() {
        return new JSONObject()
                .put("name", name)
                .put("uuid", uuid.toString())
                .put("type", type.name())
                .put("port", port)
                .put("players", players);
    }

    public static UnderstandableProxyServer singleFromJSON(JSONObject json) {
        List<UUID> players = new ArrayList<>();
        for (int i = 0; i < json.getJSONArray("players").length(); i++) {
            players.add(UUID.fromString(json.getJSONArray("players").getString(i)));
        }
        return new UnderstandableProxyServer(
                json.getString("name"),
                UUID.fromString(json.getString("uuid")),
                ServerType.valueOf(json.getString("type")),
                json.getInt("port"),
                players
        );
    }

    public static JSONObject toJSON(List<UnderstandableProxyServer> servers) {
        JSONObject json = new JSONObject();
        JSONArray serversArray = new JSONArray();
        for (UnderstandableProxyServer server : servers) {
            serversArray.put(server.toJSON());
        }
        json.put("servers", serversArray);
        return json;
    }

    public static List<UnderstandableProxyServer> fromJSON(JSONObject json) {
        List<UnderstandableProxyServer> servers = new ArrayList<>();
        JSONArray serversArray = json.getJSONArray("servers");
        for (int i = 0; i < serversArray.length(); i++) {
            servers.add(singleFromJSON(serversArray.getJSONObject(i)));
        }
        return servers;
    }
}
