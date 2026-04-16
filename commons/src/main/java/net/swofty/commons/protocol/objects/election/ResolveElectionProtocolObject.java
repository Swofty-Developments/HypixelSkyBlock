package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class ResolveElectionProtocolObject
        extends ProtocolObject<ResolveElectionProtocolObject.ResolveElectionMessage,
        ResolveElectionProtocolObject.ResolveElectionResponse> {

    @Override
    public Serializer<ResolveElectionMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(ResolveElectionMessage value) {
                JSONObject json = new JSONObject();
                json.put("year", value.year());
                return json.toString();
            }

            @Override
            public ResolveElectionMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new ResolveElectionMessage(obj.getInt("year"));
            }

            @Override
            public ResolveElectionMessage clone(ResolveElectionMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<ResolveElectionResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(ResolveElectionResponse value) {
                JSONObject json = new JSONObject();
                json.put("resolved", value.resolved());
                json.put("serializedData", value.serializedData());
                return json.toString();
            }

            @Override
            public ResolveElectionResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new ResolveElectionResponse(
                        obj.getBoolean("resolved"),
                        obj.optString("serializedData", null)
                );
            }

            @Override
            public ResolveElectionResponse clone(ResolveElectionResponse value) {
                return value;
            }
        };
    }

    public record ResolveElectionMessage(int year) {}

    public record ResolveElectionResponse(boolean resolved, String serializedData) {}
}
