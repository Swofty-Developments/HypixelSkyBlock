package net.swofty.commons.protocol.protocols.auctions;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolSpecification;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.commons.protocol.serializers.AuctionItemSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolAddItem extends ProtocolSpecification {
    @Override
    public List<ProtocolEntries<?>> getServiceProtocolEntries() {
        return new ArrayList<>(List.of(
                new ProtocolEntries<>("item", true,
                        new AuctionItemSerializer<>()),
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
