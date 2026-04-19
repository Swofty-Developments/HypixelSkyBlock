package net.swofty.commons.protocol.objects.presence;

import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class UpdatePresenceProtocolObject extends ProtocolObject<
        UpdatePresenceProtocolObject.UpdatePresenceMessage,
        UpdatePresenceProtocolObject.UpdatePresenceResponse> {

    @Override
    public Serializer<UpdatePresenceMessage> getSerializer() {
        return new JacksonSerializer<>(UpdatePresenceMessage.class);
    }

    @Override
    public Serializer<UpdatePresenceResponse> getReturnSerializer() {
        return new JacksonSerializer<>(UpdatePresenceResponse.class);
    }

    public record UpdatePresenceMessage(PresenceInfo presence) {}

    public record UpdatePresenceResponse(boolean success) {}
}
