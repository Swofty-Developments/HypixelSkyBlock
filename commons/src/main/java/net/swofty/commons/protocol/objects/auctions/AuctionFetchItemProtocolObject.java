package net.swofty.commons.protocol.objects.auctions;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class AuctionFetchItemProtocolObject extends ProtocolObject<
        AuctionFetchItemProtocolObject.AuctionFetchItemMessage,
        AuctionFetchItemProtocolObject.AuctionFetchItemResponse> {

    @Override
    public Serializer<AuctionFetchItemMessage> getSerializer() {
        return new JacksonSerializer<>(AuctionFetchItemMessage.class);
    }

    @Override
    public Serializer<AuctionFetchItemResponse> getReturnSerializer() {
        return new JacksonSerializer<>(AuctionFetchItemResponse.class);
    }

    public record AuctionFetchItemMessage(UUID uuid) {}

    public record AuctionFetchItemResponse(AuctionItem item) {}
}
