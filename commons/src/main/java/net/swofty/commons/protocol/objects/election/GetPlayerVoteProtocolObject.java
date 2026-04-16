package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class GetPlayerVoteProtocolObject
        extends ProtocolObject<GetPlayerVoteProtocolObject.GetPlayerVoteMessage,
        GetPlayerVoteProtocolObject.GetPlayerVoteResponse> {

    @Override
    public Serializer<GetPlayerVoteMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetPlayerVoteMessage value) {
                JSONObject json = new JSONObject();
                json.put("accountId", value.accountId().toString());
                return json.toString();
            }

            @Override
            public GetPlayerVoteMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GetPlayerVoteMessage(UUID.fromString(obj.getString("accountId")));
            }

            @Override
            public GetPlayerVoteMessage clone(GetPlayerVoteMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<GetPlayerVoteResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetPlayerVoteResponse value) {
                JSONObject json = new JSONObject();
                json.put("candidateName", value.candidateName());
                return json.toString();
            }

            @Override
            public GetPlayerVoteResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GetPlayerVoteResponse(obj.optString("candidateName", null));
            }

            @Override
            public GetPlayerVoteResponse clone(GetPlayerVoteResponse value) {
                return value;
            }
        };
    }

    public record GetPlayerVoteMessage(UUID accountId) {}

    public record GetPlayerVoteResponse(String candidateName) {}
}
