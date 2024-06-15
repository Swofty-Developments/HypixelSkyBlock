package net.swofty.commons.protocol.protocols.auctions;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionsFilter;
import net.swofty.commons.auctions.AuctionsSorting;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolSpecification;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.commons.protocol.serializers.AuctionItemListSerializer;

import java.util.ArrayList;
import java.util.List;

public class ProtocolFetchItems extends ProtocolSpecification {

    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<AuctionsSorting>("sorting", true,
                        new JacksonSerializer<>(AuctionsSorting.class)),
                new ProtocolEntries<AuctionsFilter>("filter", true,
                        new JacksonSerializer<>(AuctionsFilter.class)),
                new ProtocolEntries<AuctionCategories>("category", true,
                        new JacksonSerializer<>(AuctionCategories.class))
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<List<AuctionItem>>("items", true,
                        new AuctionItemListSerializer())
        ));
    }

    @Override
    public String getEndpoint() {
        return "fetch-items";
    }
}
