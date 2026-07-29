package net.swofty.service.auction.endpoints;

import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocol;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.auction.AuctionInactiveDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.bson.Document;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointFetchItem implements RedisMessageHandler<
        AuctionFetchItemProtocol.AuctionFetchItemMessage,
        AuctionFetchItemProtocol.AuctionFetchItemResponse> {

    @Override
    public AuctionFetchItemProtocol protocol() {
        return new AuctionFetchItemProtocol();
    }

    @Override
    public AuctionFetchItemProtocol.AuctionFetchItemResponse handle(AuctionFetchItemProtocol.AuctionFetchItemMessage messageObject, RedisMessageContext context) {
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

        return new AuctionFetchItemProtocol.AuctionFetchItemResponse(toReturn, true, null);
    }
}
