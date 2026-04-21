package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.Base64;

public class ReplayMapLoadProtocolObject extends ProtocolObject<
    ReplayMapLoadProtocolObject.MapLoadRequest,
    ReplayMapLoadProtocolObject.MapLoadResponse> {
    @Override
    public Serializer<MapLoadRequest> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(MapLoadRequest value) {
                JSONObject json = new JSONObject();
                json.put("mapHash", value.mapHash);
                return json.toString();
            }

            @Override
            public MapLoadRequest deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new MapLoadRequest(obj.getString("mapHash"));
            }

            @Override
            public MapLoadRequest clone(MapLoadRequest value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<MapLoadResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(MapLoadResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("found", value.found);
                json.put("mapHash", value.mapHash != null ? value.mapHash : "");
                json.put("data", value.compressedData != null ?
                    Base64.getEncoder().encodeToString(value.compressedData) : "");
                json.put("uncompressedSize", value.uncompressedSize);
                return json.toString();
            }

            @Override
            public MapLoadResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                String data = obj.optString("data", "");
                return new MapLoadResponse(
                    obj.getBoolean("success"),
                    obj.getBoolean("found"),
                    obj.optString("mapHash", null),
                    data.isEmpty() ? null : Base64.getDecoder().decode(data),
                    obj.optInt("uncompressedSize", 0)
                );
            }

            @Override
            public MapLoadResponse clone(MapLoadResponse value) {
                return value;
            }
        };
    }

    public record MapLoadRequest(String mapHash) {
    }

    public record MapLoadResponse(
        boolean success,
        boolean found,
        String mapHash,
        byte[] compressedData,
        int uncompressedSize
    ) {
    }
}
