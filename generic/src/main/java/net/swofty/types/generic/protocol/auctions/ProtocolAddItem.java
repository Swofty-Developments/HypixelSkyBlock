package net.swofty.types.generic.protocol.auctions;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionsFilter;
import net.swofty.commons.auctions.AuctionsSorting;
import net.swofty.service.generic.ProtocolSpecification;
import net.swofty.types.generic.auction.AuctionItem;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolAddItem extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<JSONObject>("item", true),
                new ProtocolEntries<String>("category", true)
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<UUID>("uuid", true)
        ));
    }

    @Override
    public String getEndpoint() {
        return "add-item";
    }
}
