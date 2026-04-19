package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class BazaarBuyProtocolObject extends ProtocolObject
        <BazaarBuyProtocolObject.BazaarBuyMessage, BazaarBuyProtocolObject.BazaarBuyResponse> {

    @Override
    public Serializer<BazaarBuyMessage> getSerializer() {
        return new JacksonSerializer<>(BazaarBuyMessage.class);
    }

    @Override
    public Serializer<BazaarBuyResponse> getReturnSerializer() {
        return new JacksonSerializer<>(BazaarBuyResponse.class);
    }

    public record BazaarBuyMessage(String itemName, int amount, double price, UUID playerUUID, UUID profileUUID) {}

    public record BazaarBuyResponse(boolean successful) {}
}
