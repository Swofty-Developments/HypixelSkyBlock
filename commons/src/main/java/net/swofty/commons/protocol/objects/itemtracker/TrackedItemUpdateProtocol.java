package net.swofty.commons.protocol.objects.itemtracker;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class TrackedItemUpdateProtocol extends RedisProtocol<
        TrackedItemUpdateProtocol.TrackedItemUpdateMessage,
        TrackedItemUpdateProtocol.TrackedItemUpdateResponse> {
    private static final Serializer<TrackedItemUpdateMessage> SERIALIZER =
            new JacksonSerializer<>(TrackedItemUpdateMessage.class);
    private static final Serializer<TrackedItemUpdateResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(TrackedItemUpdateResponse.class);

    @Override
    public Serializer<TrackedItemUpdateMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<TrackedItemUpdateResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record TrackedItemUpdateMessage(
            UUID itemUUID,
            UUID attachedPlayerUUID,
            UUID attachedPlayerProfile,
            String itemType
    ) { }

    public record TrackedItemUpdateResponse() { }
}
