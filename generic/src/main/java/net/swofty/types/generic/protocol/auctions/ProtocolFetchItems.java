package net.swofty.types.generic.protocol.auctions;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionsFilter;
import net.swofty.commons.auctions.AuctionsSorting;
import net.swofty.service.generic.ProtocolSpecification;
import net.swofty.types.generic.auction.AuctionItem;

import java.util.ArrayList;
import java.util.List;

public class ProtocolFetchItems extends ProtocolSpecification {

    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<AuctionsSorting>("sorting", true),
                new ProtocolEntries<AuctionsFilter>("filter", true),
                new ProtocolEntries<AuctionCategories>("category", true)
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<List<String>>("items", true)
        ));
    }

    @Override
    public String getEndpoint() {
        return "fetch-items";
    }
}
