package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public class BazaarGetItemProtocolObject extends ProtocolObject<
        BazaarGetItemProtocolObject.BazaarGetItemMessage,
        BazaarGetItemProtocolObject.BazaarGetItemResponse> {

    @Override
    public Serializer<BazaarGetItemMessage> getSerializer() {
        return new JacksonSerializer<>(BazaarGetItemMessage.class);
    }

    @Override
    public Serializer<BazaarGetItemResponse> getReturnSerializer() {
        return new JacksonSerializer<>(BazaarGetItemResponse.class);
    }

    public record BazaarGetItemMessage(String itemName) {}

    public record BazaarGetItemResponse(
            String itemName,
            List<OrderRecord> buyOrders,
            List<OrderRecord> sellOrders
    ) {}

    public record OrderRecord(
            UUID playerUUID,
            UUID profileUUID,
            double price,
            double amount
    ) {}
}
