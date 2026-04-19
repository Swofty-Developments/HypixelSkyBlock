package net.swofty.commons.protocol.objects.auctions;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionsFilter;
import net.swofty.commons.skyblock.auctions.AuctionsSorting;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.skyblock.auctions.AuctionItem;

import java.util.List;

public class AuctionFetchItemsProtocolObject extends ProtocolObject<
        AuctionFetchItemsProtocolObject.AuctionFetchItemsMessage,
        AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse> {

    @Override
    public Serializer<AuctionFetchItemsMessage> getSerializer() {
        return new JacksonSerializer<>(AuctionFetchItemsMessage.class);
    }

    @Override
    public Serializer<AuctionFetchItemsResponse> getReturnSerializer() {
        return new JacksonSerializer<>(AuctionFetchItemsResponse.class);
    }

    public record AuctionFetchItemsMessage(
            AuctionsSorting sorting,
            AuctionsFilter filter,
            AuctionCategories category
    ) {}

    public record AuctionFetchItemsResponse(
            List<AuctionItem> items
    ) {}
}
