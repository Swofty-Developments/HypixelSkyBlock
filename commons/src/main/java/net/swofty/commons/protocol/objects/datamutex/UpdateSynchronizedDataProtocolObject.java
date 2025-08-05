package net.swofty.commons.protocol.objects.datamutex;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdateSynchronizedDataProtocolObject extends ProtocolObject<
        UpdateSynchronizedDataProtocolObject.UpdateDataRequest,
        UpdateSynchronizedDataProtocolObject.UpdateDataResponse> {

    @Override
    public Serializer<UpdateDataRequest> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(UpdateDataRequest value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUUID.toString());
                json.put("dataKey", value.dataKey);
                json.put("newData", value.newData);

                JSONArray servers = new JSONArray();
                value.serverUUIDs.forEach(uuid -> servers.put(uuid.toString()));
                json.put("serverUUIDs", servers);

                return json.toString();
            }

            @Override
            public UpdateDataRequest deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                UUID playerUUID = UUID.fromString(obj.getString("playerUUID"));
                String dataKey = obj.getString("dataKey");
                String newData = obj.getString("newData");

                List<UUID> serverUUIDs = new ArrayList<>();
                JSONArray servers = obj.getJSONArray("serverUUIDs");
                for (int i = 0; i < servers.length(); i++) {
                    serverUUIDs.add(UUID.fromString(servers.getString(i)));
                }

                return new UpdateDataRequest(serverUUIDs, playerUUID, dataKey, newData);
            }

            @Override
            public UpdateDataRequest clone(UpdateDataRequest value) {
                return new UpdateDataRequest(
                        new ArrayList<>(value.serverUUIDs),
                        value.playerUUID,
                        value.dataKey,
                        value.newData
                );
            }
        };
    }

    @Override
    public Serializer<UpdateDataResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(UpdateDataResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("message", value.message);
                return json.toString();
            }

            @Override
            public UpdateDataResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new UpdateDataResponse(
                        obj.getBoolean("success"),
                        obj.getString("message")
                );
            }

            @Override
            public UpdateDataResponse clone(UpdateDataResponse value) {
                return new UpdateDataResponse(value.success, value.message);
            }
        };
    }

    public record UpdateDataRequest(
            List<UUID> serverUUIDs,
            UUID playerUUID,
            String dataKey,
            String newData
    ) {}

    public record UpdateDataResponse(
            boolean success,
            String message
    ) {}
}