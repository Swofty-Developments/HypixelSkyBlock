package net.swofty.types.generic.protocol.auctions;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.service.protocol.JacksonSerializer;
import net.swofty.service.protocol.ProtocolSpecification;
import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.serializer.AuctionItemSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolAddItem extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<>("item", true,
                        new AuctionItemSerializer<>(AuctionItem.class)),
                new ProtocolEntries<AuctionCategories>("category", true,
                        new JacksonSerializer<>(AuctionCategories.class))
        ));
    }

    @Override
    public List<ProtocolEntries<?>> getReturnedProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<UUID>("uuid", true,
                        new JacksonSerializer<>(UUID.class))
        ));
    }

    @Override
    public String getEndpoint() {
        return "add-item";
    }
}
