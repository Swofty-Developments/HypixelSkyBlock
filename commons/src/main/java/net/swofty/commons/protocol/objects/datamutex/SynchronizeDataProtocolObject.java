package net.swofty.commons.protocol.objects.datamutex;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SynchronizeDataProtocolObject extends ProtocolObject<
        SynchronizeDataProtocolObject.SynchronizeDataRequest,
        SynchronizeDataProtocolObject.SynchronizeDataResponse> {

    @Override
    public Serializer<SynchronizeDataRequest> getSerializer() {
        return new Serializer<SynchronizeDataRequest>() {
            @Override
            public String serialize(SynchronizeDataRequest value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUUID.toString());
                json.put("dataKey", value.dataKey);

                JSONArray servers = new JSONArray();
                value.serverUUIDs.forEach(uuid -> servers.put(uuid.toString()));
                json.put("serverUUIDs", servers);

                return json.toString();
            }

            @Override
            public SynchronizeDataRequest deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                UUID playerUUID = UUID.fromString(obj.getString("playerUUID"));
                String dataKey = obj.getString("dataKey");

                List<UUID> serverUUIDs = new ArrayList<>();
                JSONArray servers = obj.getJSONArray("serverUUIDs");
                for (int i = 0; i < servers.length(); i++) {
                    serverUUIDs.add(UUID.fromString(servers.getString(i)));
                }

                return new SynchronizeDataRequest(serverUUIDs, playerUUID, dataKey);
            }

            @Override
            public SynchronizeDataRequest clone(SynchronizeDataRequest value) {
                return new SynchronizeDataRequest(
                        new ArrayList<>(value.serverUUIDs),
                        value.playerUUID,
                        value.dataKey
                );
            }
        };
    }

    @Override
    public Serializer<SynchronizeDataResponse> getReturnSerializer() {
        return new Serializer<SynchronizeDataResponse>() {
            @Override
            public String serialize(SynchronizeDataResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("message", value.message);
                if (value.synchronizedData != null) {
                    json.put("data", value.synchronizedData);
                }
                return json.toString();
            }

            @Override
            public SynchronizeDataResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new SynchronizeDataResponse(
                        obj.getBoolean("success"),
                        obj.getString("message"),
                        obj.optString("data", null)
                );
            }

            @Override
            public SynchronizeDataResponse clone(SynchronizeDataResponse value) {
                return new SynchronizeDataResponse(value.success, value.message, value.synchronizedData);
            }
        };
    }

    public record SynchronizeDataRequest(
            List<UUID> serverUUIDs,
            UUID playerUUID,
            String dataKey
    ) {}

    public record SynchronizeDataResponse(
            boolean success,
            String message,
            String synchronizedData
    ) {}
}