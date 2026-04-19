package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class BazaarCancelProtocolObject extends ProtocolObject<
        BazaarCancelProtocolObject.CancelMessage,
        BazaarCancelProtocolObject.CancelResponse> {

    @Override
    public Serializer<CancelMessage> getSerializer() {
        return new JacksonSerializer<>(CancelMessage.class);
    }

    @Override
    public Serializer<CancelResponse> getReturnSerializer() {
        return new JacksonSerializer<>(CancelResponse.class);
    }

    public record CancelMessage(UUID orderId, UUID playerUuid, UUID profileUuid) {}

    public record CancelResponse(boolean successful) {}
}
