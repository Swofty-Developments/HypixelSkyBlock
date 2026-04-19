package net.swofty.commons.protocol.objects.auctions;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class AuctionAddItemProtocolObject extends ProtocolObject
        <AuctionAddItemProtocolObject.AuctionAddItemMessage,
        AuctionAddItemProtocolObject.AuctionAddItemResponse> {

    @Override
    public Serializer<AuctionAddItemMessage> getSerializer() {
        return new JacksonSerializer<>(AuctionAddItemMessage.class);
    }

    @Override
    public Serializer<AuctionAddItemResponse> getReturnSerializer() {
        return new JacksonSerializer<>(AuctionAddItemResponse.class);
    }

    public record AuctionAddItemMessage(AuctionItem item, AuctionCategories category) {}

    public record AuctionAddItemResponse(UUID uuid) {}
}
