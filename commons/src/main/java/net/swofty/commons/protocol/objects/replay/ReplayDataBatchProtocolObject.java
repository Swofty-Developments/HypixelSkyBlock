package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.Base64;
import java.util.UUID;

public class ReplayDataBatchProtocolObject extends ProtocolObject<
        ReplayDataBatchProtocolObject.BatchMessage,
        ReplayDataBatchProtocolObject.BatchResponse> {

    @Override
    public Serializer<BatchMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BatchMessage value) {
                JSONObject json = new JSONObject();
                json.put("replayId", value.replayId.toString());
                json.put("batchIndex", value.batchIndex);
                json.put("startTick", value.startTick);
                json.put("endTick", value.endTick);
                json.put("recordableCount", value.recordableCount);
                // Base64 encode binary data for JSON transport
                json.put("data", Base64.getEncoder().encodeToString(value.data));
                return json.toString();
            }

            @Override
            public BatchMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new BatchMessage(
                        UUID.fromString(obj.getString("replayId")),
                        obj.getInt("batchIndex"),
                        obj.getInt("startTick"),
                        obj.getInt("endTick"),
                        obj.getInt("recordableCount"),
                        Base64.getDecoder().decode(obj.getString("data"))
                );
            }

            @Override
            public BatchMessage clone(BatchMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<BatchResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BatchResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("bytesReceived", value.bytesReceived);
                return json.toString();
            }

            @Override
            public BatchResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new BatchResponse(obj.getBoolean("success"), obj.getLong("bytesReceived"));
            }

            @Override
            public BatchResponse clone(BatchResponse value) {
                return value;
            }
        };
    }

    public record BatchMessage(
            UUID replayId,
            int batchIndex,
            int startTick,
            int endTick,
            int recordableCount,
            byte[] data // Compressed recordable data
    ) {}

    public record BatchResponse(boolean success, long bytesReceived) {}
}
