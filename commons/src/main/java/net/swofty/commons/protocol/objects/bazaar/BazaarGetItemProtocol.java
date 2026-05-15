package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class BazaarGetItemProtocol extends RedisProtocol<
        BazaarGetItemProtocol.BazaarGetItemMessage,
        BazaarGetItemProtocol.BazaarGetItemResponse> {
    private static final Serializer<BazaarGetItemMessage> SERIALIZER =
            new JacksonSerializer<>(BazaarGetItemMessage.class);
    private static final Serializer<BazaarGetItemResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(BazaarGetItemResponse.class);

    @Override
    public Serializer<BazaarGetItemMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<BazaarGetItemResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record BazaarGetItemMessage(String itemName) {}

    public record BazaarGetItemResponse(
            String itemName,
            List<OrderRecord> buyOrders,
            List<OrderRecord> sellOrders,
            boolean success,
            @Nullable String error
    ) {}

    public record OrderRecord(
            UUID playerUUID,
            UUID profileUUID,
            double price,
            double amount
    ) {}
}
