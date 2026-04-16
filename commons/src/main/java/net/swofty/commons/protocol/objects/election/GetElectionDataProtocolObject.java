package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class GetElectionDataProtocolObject
        extends ProtocolObject<GetElectionDataProtocolObject.GetElectionDataMessage,
        GetElectionDataProtocolObject.GetElectionDataResponse> {

    @Override
    public Serializer<GetElectionDataMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetElectionDataMessage value) {
                return "";
            }

            @Override
            public GetElectionDataMessage deserialize(String json) {
                return new GetElectionDataMessage();
            }

            @Override
            public GetElectionDataMessage clone(GetElectionDataMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<GetElectionDataResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetElectionDataResponse value) {
                JSONObject json = new JSONObject();
                json.put("found", value.found());
                json.put("data", value.serializedData());
                return json.toString();
            }

            @Override
            public GetElectionDataResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GetElectionDataResponse(
                        obj.getBoolean("found"),
                        obj.optString("data", null)
                );
            }

            @Override
            public GetElectionDataResponse clone(GetElectionDataResponse value) {
                return value;
            }
        };
    }

    public record GetElectionDataMessage() {}

    public record GetElectionDataResponse(
            boolean found,
            String serializedData
    ) {}
}
