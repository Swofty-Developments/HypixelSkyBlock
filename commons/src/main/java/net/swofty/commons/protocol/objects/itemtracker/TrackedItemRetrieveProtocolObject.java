package net.swofty.commons.protocol.objects.itemtracker;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.bson.Document;

import java.util.UUID;

public class TrackedItemRetrieveProtocolObject extends ProtocolObject
        <TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage,
        TrackedItemRetrieveProtocolObject.TrackedItemResponse> {


    @Override
    public Serializer<TrackedItemRetrieveMessage> getSerializer() {
        return new Serializer<TrackedItemRetrieveMessage>() {
            @Override
            public String serialize(TrackedItemRetrieveMessage value) {
                return value.itemUUID.toString();
            }

            @Override
            public TrackedItemRetrieveMessage deserialize(String json) {
                return new TrackedItemRetrieveMessage(UUID.fromString(json));
            }

            @Override
            public TrackedItemRetrieveMessage clone(TrackedItemRetrieveMessage value) {
                return new TrackedItemRetrieveMessage(value.itemUUID);
            }
        };
    }

    @Override
    public Serializer<TrackedItemResponse> getReturnSerializer() {
        return new Serializer<TrackedItemResponse>() {
            @Override
            public String serialize(TrackedItemResponse value) {
                return value.trackedItem.toDocument().toJson();
            }

            @Override
            public TrackedItemResponse deserialize(String json) {
                return new TrackedItemResponse(TrackedItem.fromDocument(Document.parse(json)));
            }

            @Override
            public TrackedItemResponse clone(TrackedItemResponse value) {
                return new TrackedItemResponse(value.trackedItem.clone());
            }
        };
    }

    public record TrackedItemRetrieveMessage(UUID itemUUID) {
        public TrackedItemRetrieveMessage(String itemUUID) {
            this(UUID.fromString(itemUUID));
        }
    }

    public record TrackedItemResponse(TrackedItem trackedItem) { }
}
