package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class StartElectionProtocolObject
        extends ProtocolObject<StartElectionProtocolObject.StartElectionMessage,
        StartElectionProtocolObject.StartElectionResponse> {

    @Override
    public Serializer<StartElectionMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(StartElectionMessage value) {
                JSONObject json = new JSONObject();
                json.put("year", value.year());
                json.put("candidatesJson", value.candidatesJson());
                return json.toString();
            }

            @Override
            public StartElectionMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new StartElectionMessage(
                        obj.getInt("year"),
                        obj.getString("candidatesJson")
                );
            }

            @Override
            public StartElectionMessage clone(StartElectionMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<StartElectionResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(StartElectionResponse value) {
                JSONObject json = new JSONObject();
                json.put("started", value.started());
                json.put("serializedData", value.serializedData());
                return json.toString();
            }

            @Override
            public StartElectionResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new StartElectionResponse(
                        obj.getBoolean("started"),
                        obj.optString("serializedData", null)
                );
            }

            @Override
            public StartElectionResponse clone(StartElectionResponse value) {
                return value;
            }
        };
    }

    public record StartElectionMessage(int year, String candidatesJson) {}

    public record StartElectionResponse(boolean started, String serializedData) {}
}
