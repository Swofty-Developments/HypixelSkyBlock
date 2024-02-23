package net.swofty.types.generic.protocol.bazaar;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionsFilter;
import net.swofty.commons.auctions.AuctionsSorting;
import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.service.generic.ProtocolSpecification;

import java.util.ArrayList;
import java.util.List;

public class ProtocolInitializeBazaarCheck extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<String>("init-request", true)
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>();
    }

    @Override
    public String getEndpoint() {
        return "bazaar-initialize";
    }
}
