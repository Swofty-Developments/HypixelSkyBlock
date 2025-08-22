package net.swofty.commons.protocol.objects.experimentation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChronomatronGetStateProtocolObject extends ProtocolObject<
        ChronomatronGetStateProtocolObject.GetStateMessage,
        ChronomatronGetStateProtocolObject.GetStateResponse
        > {

    @Override
    public String channel() {
        return "experimentation_chronomatron_get_state";
    }

    @Override
    public Serializer<GetStateMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetStateMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUUID.toString());
                return json.toString();
            }

            @Override
            public GetStateMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GetStateMessage msg = new GetStateMessage();
                msg.playerUUID = UUID.fromString(obj.getString("playerUUID"));
                return msg;
            }

            @Override
            public GetStateMessage clone(GetStateMessage value) {
                return new GetStateMessage(value.playerUUID);
            }
        };
    }

    @Override
    public Serializer<GetStateResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetStateResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("error", value.errorMessage);
                json.put("sequence", value.sequence);
                json.put("playerInputPosition", value.playerInputPosition);
                return json.toString();
            }

            @Override
            public GetStateResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GetStateResponse res = new GetStateResponse();
                res.success = obj.getBoolean("success");
                res.errorMessage = obj.optString("error", null);
                res.sequence = new ArrayList<>();
                JSONArray arr = obj.optJSONArray("sequence");
                if (arr != null) for (int i = 0; i < arr.length(); i++) res.sequence.add(arr.getInt(i));
                res.playerInputPosition = obj.optInt("playerInputPosition", 0);
                return res;
            }

            @Override
            public GetStateResponse clone(GetStateResponse value) {
                return new GetStateResponse(value.success, new ArrayList<>(value.sequence), value.playerInputPosition, value.errorMessage);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetStateMessage {
        public UUID playerUUID;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetStateResponse {
        public boolean success;
        public List<Integer> sequence;
        public int playerInputPosition;
        public String errorMessage;
    }
}


