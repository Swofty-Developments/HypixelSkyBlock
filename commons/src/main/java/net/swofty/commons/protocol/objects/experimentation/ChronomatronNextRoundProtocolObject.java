package net.swofty.commons.protocol.objects.experimentation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class ChronomatronNextRoundProtocolObject extends ProtocolObject<
        ChronomatronNextRoundProtocolObject.NextRoundMessage,
        ChronomatronNextRoundProtocolObject.NextRoundResponse
        > {

    @Override
    public String channel() {
        return "experimentation_chronomatron_next_round";
    }

    @Override
    public Serializer<NextRoundMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(NextRoundMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUUID.toString());
                return json.toString();
            }

            @Override
            public NextRoundMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                NextRoundMessage msg = new NextRoundMessage();
                msg.playerUUID = UUID.fromString(obj.getString("playerUUID"));
                return msg;
            }

            @Override
            public NextRoundMessage clone(NextRoundMessage value) {
                return new NextRoundMessage(value.playerUUID);
            }
        };
    }

    @Override
    public Serializer<NextRoundResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(NextRoundResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("error", value.errorMessage);
                return json.toString();
            }

            @Override
            public NextRoundResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                NextRoundResponse res = new NextRoundResponse();
                res.success = obj.getBoolean("success");
                res.errorMessage = obj.optString("error", null);
                return res;
            }

            @Override
            public NextRoundResponse clone(NextRoundResponse value) {
                return new NextRoundResponse(value.success, value.errorMessage);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class NextRoundMessage {
        public UUID playerUUID;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class NextRoundResponse {
        public boolean success;
        public String errorMessage;
    }
}


