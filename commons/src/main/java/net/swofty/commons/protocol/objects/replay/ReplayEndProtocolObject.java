package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class ReplayEndProtocolObject extends ProtocolObject<
        ReplayEndProtocolObject.EndMessage,
        ReplayEndProtocolObject.EndResponse> {

    @Override
    public Serializer<EndMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(EndMessage value) {
                JSONObject json = new JSONObject();
                json.put("replayId", value.replayId.toString());
                json.put("endTime", value.endTime);
                json.put("durationTicks", value.durationTicks);
                json.put("winnerId", value.winnerId != null ? value.winnerId : "");
                json.put("winnerType", value.winnerType != null ? value.winnerType : "");
                return json.toString();
            }

            @Override
            public EndMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                String winnerId = obj.optString("winnerId", null);
                String winnerType = obj.optString("winnerType", null);
                return new EndMessage(
                        UUID.fromString(obj.getString("replayId")),
                        obj.getLong("endTime"),
                        obj.getInt("durationTicks"),
                        winnerId != null && !winnerId.isEmpty() ? winnerId : null,
                        winnerType != null && !winnerType.isEmpty() ? winnerType : null
                );
            }

            @Override
            public EndMessage clone(EndMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<EndResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(EndResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("totalBytes", value.totalBytes);
                json.put("compressedBytes", value.compressedBytes);
                json.put("available", value.available);
                return json.toString();
            }

            @Override
            public EndResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new EndResponse(
                        obj.getBoolean("success"),
                        obj.getLong("totalBytes"),
                        obj.getLong("compressedBytes"),
                        obj.getBoolean("available")
                );
            }

            @Override
            public EndResponse clone(EndResponse value) {
                return value;
            }
        };
    }

    public record EndMessage(
            UUID replayId,
            long endTime,
            int durationTicks,
            String winnerId,    // Team ID or player UUID
            String winnerType   // "TEAM" or "PLAYER"
    ) {}

    public record EndResponse(
            boolean success,
            long totalBytes,
            long compressedBytes,
            boolean available   // Whether the replay is ready for viewing
    ) {}
}
