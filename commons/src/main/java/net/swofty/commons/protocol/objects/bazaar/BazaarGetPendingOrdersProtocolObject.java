package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class BazaarGetPendingOrdersProtocolObject
        extends ProtocolObject<
        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage,
        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse> {
    private static final Serializer<BazaarGetPendingOrdersMessage> SERIALIZER =
            new JacksonSerializer<>(BazaarGetPendingOrdersMessage.class);
    private static final Serializer<BazaarGetPendingOrdersResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(BazaarGetPendingOrdersResponse.class);

    @Override
    public Serializer<BazaarGetPendingOrdersMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<BazaarGetPendingOrdersResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
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

    public record BazaarGetPendingOrdersResponse(List<PendingOrder> orders, boolean success, @Nullable String error) {}
}
