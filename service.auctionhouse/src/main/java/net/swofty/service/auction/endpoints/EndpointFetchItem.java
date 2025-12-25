package net.swofty.service.auction.endpoints;

import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.auction.AuctionInactiveDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.UUID;

public class EndpointFetchItem implements ServiceEndpoint<
        AuctionFetchItemProtocolObject.AuctionFetchItemMessage,
        AuctionFetchItemProtocolObject.AuctionFetchItemResponse> {

    @Override
    public AuctionFetchItemProtocolObject associatedProtocolObject() {
        return new AuctionFetchItemProtocolObject();
    }

    @Override
    public AuctionFetchItemProtocolObject.AuctionFetchItemResponse onMessage(ServiceProxyRequest message, AuctionFetchItemProtocolObject.AuctionFetchItemMessage messageObject) {
        UUID uuidToFetch = messageObject.uuid();

        AuctionItem toReturn = new AuctionItem();

        Document item = AuctionActiveDatabase.collection.find(new Document("_id", uuidToFetch.toString())).first();
        if (item != null) {
            toReturn = AuctionItem.fromDocument(item);
        }

        Document inactiveItem = AuctionInactiveDatabase.collection.find(new Document("_id", uuidToFetch.toString())).first();
        if (inactiveItem != null) {
            toReturn = AuctionItem.fromDocument(inactiveItem);
        }

        return new AuctionFetchItemProtocolObject.AuctionFetchItemResponse(toReturn);
    }
}
