package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class GameHeartbeatProtocolObject extends ProtocolObject
        <GameHeartbeatProtocolObject.HeartbeatMessage,
                GameHeartbeatProtocolObject.HeartbeatResponse> {

    @Override
    public Serializer<HeartbeatMessage> getSerializer() {
        return new Serializer<HeartbeatMessage>() {
            @Override
            public String serialize(HeartbeatMessage value) {
                JSONObject json = new JSONObject();
                json.put("uuid", value.uuid.toString());
                json.put("shortName", value.shortName);
                json.put("type", value.type.name());
                json.put("maps", new JSONArray(value.maps));
                if (value.mode != null) json.put("mode", value.mode);
                json.put("maxPlayers", value.maxPlayers);
                json.put("onlinePlayers", value.onlinePlayers);
                return json.toString();
            }

            @Override
            public HeartbeatMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                UUID uuid = UUID.fromString(obj.getString("uuid"));
                String shortName = obj.getString("shortName");
                ServerType type = ServerType.valueOf(obj.getString("type"));
                List<String> maps = new ArrayList<>();
                JSONArray arr = obj.getJSONArray("maps");
                for (int i = 0; i < arr.length(); i++) maps.add(arr.getString(i));
                String mode = obj.has("mode") ? obj.getString("mode") : null;
                int max = obj.getInt("maxPlayers");
                int online = obj.getInt("onlinePlayers");
                return new HeartbeatMessage(uuid, shortName, type, maps, mode, max, online);
            }

            @Override
            public HeartbeatMessage clone(HeartbeatMessage value) {
                return new HeartbeatMessage(value.uuid, value.shortName, value.type, new ArrayList<>(value.maps), value.mode, value.maxPlayers, value.onlinePlayers);
            }
        };
    }

    @Override
    public Serializer<HeartbeatResponse> getReturnSerializer() {
        return new Serializer<HeartbeatResponse>() {
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

    public record HeartbeatMessage(UUID uuid, String shortName, ServerType type, List<String> maps, String mode, int maxPlayers, int onlinePlayers) { }

    public record HeartbeatResponse(boolean ok) { }
}
