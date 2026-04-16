package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CastVoteProtocolObject
        extends ProtocolObject<CastVoteProtocolObject.CastVoteMessage,
        CastVoteProtocolObject.CastVoteResponse> {

    @Override
    public Serializer<CastVoteMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(CastVoteMessage value) {
                JSONObject json = new JSONObject();
                json.put("accountId", value.accountId().toString());
                json.put("candidateName", value.candidateName());
                return json.toString();
            }

            @Override
            public CastVoteMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new CastVoteMessage(
                        UUID.fromString(obj.getString("accountId")),
                        obj.getString("candidateName")
                );
            }

            @Override
            public CastVoteMessage clone(CastVoteMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<CastVoteResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(CastVoteResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success());
                json.put("tallies", value.tallies() == null ? null : new JSONObject(value.tallies()));
                return json.toString();
            }

            @Override
            public CastVoteResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                Map<String, Long> tallies = null;
                if (!obj.isNull("tallies")) {
                    tallies = new HashMap<>();
                    JSONObject talliesObject = obj.getJSONObject("tallies");
                    for (String key : talliesObject.keySet()) {
                        tallies.put(key, talliesObject.getLong(key));
                    }
                }
                return new CastVoteResponse(
                        obj.getBoolean("success"),
                    tallies
                );
            }

            @Override
            public CastVoteResponse clone(CastVoteResponse value) {
                return value;
            }
        };
    }

    public record CastVoteMessage(UUID accountId, String candidateName) {}

    public record CastVoteResponse(boolean success, Map<String, Long> tallies) {
    }
}
