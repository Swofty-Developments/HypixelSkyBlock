package net.swofty.commons.protocol.objects.datamutex;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnlockDataProtocolObject extends ProtocolObject<
        UnlockDataProtocolObject.UnlockDataRequest,
        UnlockDataProtocolObject.UnlockDataResponse> {

    @Override
    public Serializer<UnlockDataRequest> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(UnlockDataRequest value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUUID.toString());
                json.put("dataKey", value.dataKey);

                JSONArray servers = new JSONArray();
                value.serverUUIDs.forEach(uuid -> servers.put(uuid.toString()));
                json.put("serverUUIDs", servers);

                return json.toString();
            }

            @Override
            public UnlockDataRequest deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                UUID playerUUID = UUID.fromString(obj.getString("playerUUID"));
                String dataKey = obj.getString("dataKey");

                List<UUID> serverUUIDs = new ArrayList<>();
                JSONArray servers = obj.getJSONArray("serverUUIDs");
                for (int i = 0; i < servers.length(); i++) {
                    serverUUIDs.add(UUID.fromString(servers.getString(i)));
                }

                return new UnlockDataRequest(serverUUIDs, playerUUID, dataKey);
            }

            @Override
            public UnlockDataRequest clone(UnlockDataRequest value) {
                return new UnlockDataRequest(
                        new ArrayList<>(value.serverUUIDs),
                        value.playerUUID,
                        value.dataKey
                );
            }
        };
    }

    @Override
    public Serializer<UnlockDataResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(UnlockDataResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("message", value.message);
                return json.toString();
            }

            @Override
            public UnlockDataResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new UnlockDataResponse(
                        obj.getBoolean("success"),
                        obj.getString("message")
                );
            }

            @Override
            public UnlockDataResponse clone(UnlockDataResponse value) {
                return new UnlockDataResponse(value.success, value.message);
            }
        };
    }

    public record UnlockDataRequest(
            List<UUID> serverUUIDs,
            UUID playerUUID,
            String dataKey
    ) {}

    public record UnlockDataResponse(
            boolean success,
            String message
    ) {}
}