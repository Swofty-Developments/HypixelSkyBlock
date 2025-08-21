package net.swofty.commons.protocol.objects.experimentation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class ChronomatronFinishProtocolObject extends ProtocolObject<
        ChronomatronFinishProtocolObject.FinishMessage,
        ChronomatronFinishProtocolObject.FinishResponse
        > {

    @Override
    public String channel() { return "experimentation_chronomatron_finish"; }

    @Override
    public Serializer<FinishMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FinishMessage value) {
                return new JSONObject()
                        .put("playerUUID", value.playerUUID.toString())
                        .toString();
            }

            @Override
            public FinishMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                FinishMessage msg = new FinishMessage();
                msg.playerUUID = UUID.fromString(obj.getString("playerUUID"));
                return msg;
            }

            @Override
            public FinishMessage clone(FinishMessage value) {
                return new FinishMessage(value.playerUUID);
            }
        };
    }

    @Override
    public Serializer<FinishResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FinishResponse value) {
                return new JSONObject()
                        .put("success", value.success)
                        .put("bestChain", value.bestChain)
                        .put("xpAward", value.xpAward)
                        .put("error", value.errorMessage)
                        .toString();
            }

            @Override
            public FinishResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                FinishResponse res = new FinishResponse();
                res.success = obj.getBoolean("success");
                res.bestChain = obj.getInt("bestChain");
                res.xpAward = obj.getInt("xpAward");
                res.errorMessage = obj.optString("error", null);
                return res;
            }

            @Override
            public FinishResponse clone(FinishResponse value) {
                return new FinishResponse(value.success, value.bestChain, value.xpAward, value.errorMessage);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinishMessage { public UUID playerUUID; }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinishResponse {
        public boolean success;
        public int bestChain;
        public int xpAward;
        public String errorMessage;
    }
}


