package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.Base64;

public class ReplayMapUploadProtocolObject extends ProtocolObject<
        ReplayMapUploadProtocolObject.MapUploadMessage,
        ReplayMapUploadProtocolObject.MapUploadResponse> {

    @Override
    public Serializer<MapUploadMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(MapUploadMessage value) {
                JSONObject json = new JSONObject();
                json.put("mapHash", value.mapHash);
                json.put("mapName", value.mapName);
                json.put("data", Base64.getEncoder().encodeToString(value.compressedData));
                return json.toString();
            }

            @Override
            public MapUploadMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new MapUploadMessage(
                        obj.getString("mapHash"),
                        obj.getString("mapName"),
                        Base64.getDecoder().decode(obj.getString("data"))
                );
            }

            @Override
            public MapUploadMessage clone(MapUploadMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<MapUploadResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(MapUploadResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("alreadyExists", value.alreadyExists);
                return json.toString();
            }

            @Override
            public MapUploadResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new MapUploadResponse(
                        obj.getBoolean("success"),
                        obj.getBoolean("alreadyExists")
                );
            }

            @Override
            public MapUploadResponse clone(MapUploadResponse value) {
                return value;
            }
        };
    }

    public record MapUploadMessage(
            String mapHash,
            String mapName,
            byte[] compressedData
    ) {}

    public record MapUploadResponse(boolean success, boolean alreadyExists) {}
}
