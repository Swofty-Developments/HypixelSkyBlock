package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BazaarBuyProtocol extends RedisProtocol
        <BazaarBuyProtocol.BazaarBuyMessage, BazaarBuyProtocol.BazaarBuyResponse> {
    private static final Serializer<BazaarBuyMessage> SERIALIZER =
            new JacksonSerializer<>(BazaarBuyMessage.class);
    private static final Serializer<BazaarBuyResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(BazaarBuyResponse.class);

    @Override

    public Serializer<BazaarBuyMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<BazaarBuyResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record BazaarBuyMessage(String itemName, int amount, double price, UUID playerUUID, UUID profileUUID) {}

    public record BazaarBuyResponse(boolean success, @Nullable String error) {}
}
