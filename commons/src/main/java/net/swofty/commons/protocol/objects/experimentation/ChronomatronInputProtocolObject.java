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

public class ChronomatronInputProtocolObject extends ProtocolObject<
        ChronomatronInputProtocolObject.InputMessage,
        ChronomatronInputProtocolObject.InputResponse
        > {

    @Override
    public String channel() {
        return "experimentation_chronomatron_input";
    }

    @Override
    public Serializer<InputMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(InputMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUUID.toString());
                json.put("inputs", value.inputs);
                return json.toString();
            }

            @Override
            public InputMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                InputMessage msg = new InputMessage();
                msg.playerUUID = UUID.fromString(obj.getString("playerUUID"));
                JSONArray arr = obj.getJSONArray("inputs");
                msg.inputs = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) msg.inputs.add(arr.getInt(i));
                return msg;
            }

            @Override
            public InputMessage clone(InputMessage value) {
                return new InputMessage(value.playerUUID, new ArrayList<>(value.inputs));
            }
        };
    }

    @Override
    public Serializer<InputResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(InputResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("complete", value.complete);
                json.put("error", value.errorMessage);
                return json.toString();
            }

            @Override
            public InputResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                InputResponse res = new InputResponse();
                res.success = obj.getBoolean("success");
                res.complete = obj.getBoolean("complete");
                res.errorMessage = obj.optString("error", null);
                return res;
            }

            @Override
            public InputResponse clone(InputResponse value) {
                return new InputResponse(value.success, value.complete, value.errorMessage);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class InputMessage {
        public UUID playerUUID;
        public List<Integer> inputs;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class InputResponse {
        public boolean success;
        public boolean complete;
        public String errorMessage;
    }
}


