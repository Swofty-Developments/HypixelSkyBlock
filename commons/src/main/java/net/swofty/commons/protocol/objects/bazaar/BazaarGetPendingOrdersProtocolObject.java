package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public class BazaarGetPendingOrdersProtocolObject
        extends ProtocolObject<
        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage,
        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse> {

    @Override
    public Serializer<BazaarGetPendingOrdersMessage> getSerializer() {
        return new JacksonSerializer<>(BazaarGetPendingOrdersMessage.class);
    }

    @Override
    public Serializer<BazaarGetPendingOrdersResponse> getReturnSerializer() {
        return new JacksonSerializer<>(BazaarGetPendingOrdersResponse.class);
    }

    public record BazaarGetPendingOrdersMessage(UUID playerUUID, UUID profileUUID) {}

    public record PendingOrder(
            UUID orderId,
            String itemName,
            String side,
            double price,
            double amount,
            UUID profileUUID
    ) {}

    public record BazaarGetPendingOrdersResponse(List<PendingOrder> orders) {}
}
