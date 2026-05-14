package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BazaarSellProtocolObject extends ProtocolObject<
        BazaarSellProtocolObject.BazaarSellMessage,
        BazaarSellProtocolObject.BazaarSellResponse> {
    private static final Serializer<BazaarSellMessage> SERIALIZER =
            new JacksonSerializer<>(BazaarSellMessage.class);
    private static final Serializer<BazaarSellResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(BazaarSellResponse.class);

    @Override
    public Serializer<BazaarSellMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<BazaarSellResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record BazaarSellMessage(String itemName, UUID playerUUID, UUID profileUUID, Double price, int amount) {}

    public record BazaarSellResponse(boolean success, @Nullable String error) {}
}
