package net.swofty.commons.protocol.objects.presence;

import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class UpdatePresenceProtocolObject extends ProtocolObject<
        UpdatePresenceProtocolObject.UpdatePresenceMessage,
        UpdatePresenceProtocolObject.UpdatePresenceResponse> {

    @Override
    public Serializer<UpdatePresenceMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(UpdatePresenceMessage value) {
                JSONObject json = new JSONObject();
                json.put("presence", new JSONObject(PresenceInfo.getSerializer().serialize(value.presence())));
                return json.toString();
            }

            @Override
            public UpdatePresenceMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                PresenceInfo presence = PresenceInfo.getSerializer().deserialize(obj.getJSONObject("presence").toString());
                return new UpdatePresenceMessage(presence);
            }

            @Override
            public UpdatePresenceMessage clone(UpdatePresenceMessage value) {
                return new UpdatePresenceMessage(value.presence());
            }
        };
    }

    @Override
    public Serializer<UpdatePresenceResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(UpdatePresenceResponse value) {
                return value.success ? "true" : "false";
            }

            @Override
            public UpdatePresenceResponse deserialize(String json) {
                return new UpdatePresenceResponse(Boolean.parseBoolean(json));
            }

            @Override
            public UpdatePresenceResponse clone(UpdatePresenceResponse value) {
                return new UpdatePresenceResponse(value.success);
            }
        };
    }

    public record UpdatePresenceMessage(PresenceInfo presence) {}

    public record UpdatePresenceResponse(boolean success) {}
}

