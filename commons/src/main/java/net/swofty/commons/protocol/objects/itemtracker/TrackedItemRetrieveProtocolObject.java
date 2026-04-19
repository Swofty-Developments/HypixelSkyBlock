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

    @Override
    public Serializer<TrackedItemRetrieveMessage> getSerializer() {
        return new JacksonSerializer<>(TrackedItemRetrieveMessage.class);
    }

    @Override
    public Serializer<TrackedItemResponse> getReturnSerializer() {
        return new JacksonSerializer<>(TrackedItemResponse.class);
    }

    public record TrackedItemRetrieveMessage(UUID itemUUID) {
        public TrackedItemRetrieveMessage(String itemUUID) {
            this(UUID.fromString(itemUUID));
        }
    }

    public record TrackedItemResponse(TrackedItem trackedItem, boolean success, @Nullable String error) { }
}
