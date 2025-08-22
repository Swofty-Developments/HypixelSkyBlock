package net.swofty.commons.protocol.objects.experimentation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class ChronomatronStartProtocolObject extends ProtocolObject<
        ChronomatronStartProtocolObject.StartMessage,
        ChronomatronStartProtocolObject.StartResponse
        > {

    @Override
    public String channel() {
        return "experimentation_chronomatron_start";
    }

    @Override
    public Serializer<StartMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(StartMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUUID.toString());
                json.put("tier", value.tier);
                return json.toString();
            }

            @Override
            public StartMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                StartMessage msg = new StartMessage();
                msg.playerUUID = UUID.fromString(obj.getString("playerUUID"));
                msg.tier = obj.getString("tier");
                return msg;
            }

            @Override
            public StartMessage clone(StartMessage value) {
                return new StartMessage(value.playerUUID, value.tier);
            }
        };
    }

    @Override
    public Serializer<StartResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(StartResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("error", value.errorMessage);
                return json.toString();
            }

            @Override
            public StartResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                StartResponse res = new StartResponse();
                res.success = obj.getBoolean("success");
                res.errorMessage = obj.optString("error", null);
                return res;
            }

            @Override
            public StartResponse clone(StartResponse value) {
                return new StartResponse(value.success, value.errorMessage);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class StartMessage {
        public UUID playerUUID;
        public String tier;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class StartResponse {
        public boolean success;
        public String errorMessage;
    }
}


