package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BazaarSellProtocolObject extends ProtocolObject<
        BazaarSellProtocolObject.BazaarSellMessage,
        BazaarSellProtocolObject.BazaarSellResponse> {

    @Override
    public Serializer<BazaarSellMessage> getSerializer() {
        return new JacksonSerializer<>(BazaarSellMessage.class);
    }

    @Override
    public Serializer<BazaarSellResponse> getReturnSerializer() {
        return new JacksonSerializer<>(BazaarSellResponse.class);
    }

    public record BazaarSellMessage(String itemName, UUID playerUUID, UUID profileUUID, Double price, int amount) {}

    public record BazaarSellResponse(boolean success, @Nullable String error) {}
}
