package net.swofty.commons.protocol.objects.itemtracker;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class TrackedItemUpdateProtocolObject extends ProtocolObject<
        TrackedItemUpdateProtocolObject.TrackedItemUpdateMessage,
        TrackedItemUpdateProtocolObject.TrackedItemUpdateResponse> {

    @Override
    public Serializer<TrackedItemUpdateMessage> getSerializer() {
        return new JacksonSerializer<>(TrackedItemUpdateMessage.class);
    }

    @Override
    public Serializer<TrackedItemUpdateResponse> getReturnSerializer() {
        return new JacksonSerializer<>(TrackedItemUpdateResponse.class);
    }

    public record TrackedItemUpdateMessage(
            UUID itemUUID,
            UUID attachedPlayerUUID,
            UUID attachedPlayerProfile,
            String itemType
    ) { }

    public record TrackedItemUpdateResponse() { }
}
