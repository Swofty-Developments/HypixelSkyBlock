package net.swofty.commons.protocol.objects.itemtracker;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class TrackedItemRetrieveProtocolObject extends ProtocolObject
        <TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage,
        TrackedItemRetrieveProtocolObject.TrackedItemResponse> {
    private static final Serializer<TrackedItemRetrieveMessage> SERIALIZER =
            new JacksonSerializer<>(TrackedItemRetrieveMessage.class);
    private static final Serializer<TrackedItemResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(TrackedItemResponse.class);

    @Override

    public Serializer<TrackedItemRetrieveMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<TrackedItemResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record TrackedItemRetrieveMessage(UUID itemUUID) {
        public TrackedItemRetrieveMessage(String itemUUID) {
            this(UUID.fromString(itemUUID));
        }
    }

    public record TrackedItemResponse(TrackedItem trackedItem, boolean success, @Nullable String error) { }
}
