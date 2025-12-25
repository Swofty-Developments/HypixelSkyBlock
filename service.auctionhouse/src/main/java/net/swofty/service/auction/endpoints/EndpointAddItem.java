package net.swofty.service.auction.endpoints;

import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.UUID;

public class EndpointAddItem implements ServiceEndpoint<
        AuctionAddItemProtocolObject.AuctionAddItemMessage,
        AuctionAddItemProtocolObject.AuctionAddItemResponse> {

    @Override
    public AuctionAddItemProtocolObject associatedProtocolObject() {
        return new AuctionAddItemProtocolObject();
    }

    @Override
    public AuctionAddItemProtocolObject.AuctionAddItemResponse onMessage(ServiceProxyRequest message, AuctionAddItemProtocolObject.AuctionAddItemMessage messageObject) {
        AuctionItem auctionItem = messageObject.item();
        UnderstandableSkyBlockItem item = auctionItem.getItem();
        item.getAttribute("item_type").setValue("HYPERION");

        AuctionCategories category = messageObject.category();
        Document document = auctionItem.toDocument();
        document.put("category", category.name());

        Thread.startVirtualThread(() -> {
            if (AuctionActiveDatabase.collection.find(new Document("_id", document.get("_id"))).first() != null) {
                AuctionActiveDatabase.collection.replaceOne(
                        new Document("_id", document.get("_id")),
                        document
                );
            } else {
                AuctionActiveDatabase.collection.insertOne(document);
            }
        });

        return new AuctionAddItemProtocolObject.AuctionAddItemResponse(UUID.fromString((String) document.get("_id")));
    }
}
