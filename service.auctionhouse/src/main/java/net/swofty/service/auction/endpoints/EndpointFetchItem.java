package net.swofty.service.auction.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.auction.AuctionInactiveDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.types.generic.auction.AuctionItem;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EndpointFetchItem implements ServiceEndpoint {
    @Override
    public String channel() {
        return "fetch-item";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        UUID uuidToFetch = (UUID) messageData.get("uuid");

        Map<String, Object> toReturn = new HashMap<>();

        Document item = AuctionActiveDatabase.collection.find(new Document("_id", uuidToFetch.toString())).first();
        if (item != null) {
            toReturn.put("item", AuctionItem.fromDocument(item));
        }

        Document inactiveItem = AuctionInactiveDatabase.collection.find(new Document("_id", uuidToFetch.toString())).first();
        if (inactiveItem != null) {
            toReturn.put("item", AuctionItem.fromDocument(inactiveItem));
        }

        return toReturn;
    }
}
