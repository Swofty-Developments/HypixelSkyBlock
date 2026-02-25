package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class SaveElectionDataProtocolObject
        extends ProtocolObject<SaveElectionDataProtocolObject.SaveElectionDataMessage,
        SaveElectionDataProtocolObject.SaveElectionDataResponse> {

    @Override
    public Serializer<SaveElectionDataMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(SaveElectionDataMessage value) {
                JSONObject json = new JSONObject();
                json.put("data", value.serializedData());
                return json.toString();
            }

            @Override
            public SaveElectionDataMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new SaveElectionDataMessage(obj.getString("data"));
            }

            @Override
            public SaveElectionDataMessage clone(SaveElectionDataMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<SaveElectionDataResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(SaveElectionDataResponse value) {
                return new JSONObject().put("success", value.success()).toString();
            }

            @Override
            public SaveElectionDataResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new SaveElectionDataResponse(obj.getBoolean("success"));
            }

            @Override
            public SaveElectionDataResponse clone(SaveElectionDataResponse value) {
                return value;
            }
        };
    }

    public record SaveElectionDataMessage(String serializedData) {}

    public record SaveElectionDataResponse(boolean success) {}
}
