package net.swofty.commons.protocol.objects.itemtracker;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class TrackedItemUpdateProtocolObject extends ProtocolObject<
        TrackedItemUpdateProtocolObject.TrackedItemUpdateMessage,
        TrackedItemUpdateProtocolObject.TrackedItemUpdateResponse> {

    @Override
    public Serializer<TrackedItemUpdateMessage> getSerializer() {
        return new Serializer<TrackedItemUpdateMessage>() {
            @Override
            public String serialize(TrackedItemUpdateMessage value) {
                return value.itemUUID.toString() + ";" +
                        value.attachedPlayerUUID.toString() + ";" +
                        value.attachedPlayerProfile.toString() + ";" +
                        value.itemType;
            }

            @Override
            public TrackedItemUpdateMessage deserialize(String json) {
                String[] split = json.split(";");
                return new TrackedItemUpdateMessage(
                        UUID.fromString(split[0]),
                        UUID.fromString(split[1]),
                        UUID.fromString(split[2]),
                        split[3]
                );
            }

            @Override
            public TrackedItemUpdateMessage clone(TrackedItemUpdateMessage value) {
                return new TrackedItemUpdateMessage(value.itemUUID, value.attachedPlayerUUID, value.attachedPlayerProfile, value.itemType);
            }
        };
    }

    @Override
    public Serializer<TrackedItemUpdateResponse> getReturnSerializer() {
        return new Serializer<TrackedItemUpdateResponse>() {
            @Override
            public String serialize(TrackedItemUpdateResponse value) {
                return "";
            }

            @Override
            public TrackedItemUpdateResponse deserialize(String json) {
                return new TrackedItemUpdateResponse();
            }

            @Override
            public TrackedItemUpdateResponse clone(TrackedItemUpdateResponse value) {
                return new TrackedItemUpdateResponse();
            }
        };
    }

    public record TrackedItemUpdateMessage(
            UUID itemUUID,
            UUID attachedPlayerUUID,
            UUID attachedPlayerProfile,
            String itemType
    ) { }

    public record TrackedItemUpdateResponse() { }
}
