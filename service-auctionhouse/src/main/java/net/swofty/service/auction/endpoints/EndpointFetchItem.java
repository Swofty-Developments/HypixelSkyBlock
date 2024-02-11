package net.swofty.service.auction.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.auction.AuctionInactiveDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;
import org.json.JSONObject;

import java.util.UUID;

public class EndpointFetchItem implements ServiceEndpoint {
    @Override
    public String channel() {
        return "fetch-item";
    }

    @Override
    public JSONObject onMessage(ServiceProxyRequest message) {
        JSONObject json = new JSONObject(message.getMessage());
        UUID uuidToFetch = UUID.fromString(json.getString("uuid"));

        Document item = AuctionActiveDatabase.collection.find(new Document("_id", uuidToFetch.toString())).first();
        if (item != null) {
            return new JSONObject().put("item", item.toJson());
        }

        Document inactiveItem = AuctionInactiveDatabase.collection.find(new Document("_id", uuidToFetch.toString())).first();
        if (inactiveItem != null) {
            return new JSONObject().put("item", inactiveItem.toJson());
        }

        return new JSONObject().put("item", "null");
    }
}
