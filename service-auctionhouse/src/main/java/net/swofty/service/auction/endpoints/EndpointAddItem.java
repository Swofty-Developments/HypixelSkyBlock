package net.swofty.service.auction.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;
import org.json.JSONObject;

public class EndpointAddItem implements ServiceEndpoint {
    @Override
    public String channel() {
        return "add-item";
    }

    @Override
    public JSONObject onMessage(ServiceProxyRequest message) {
        JSONObject json = new JSONObject(message.getMessage());
        Document item = new Document(json.getJSONObject("item").toMap());

        AuctionActiveDatabase.collection.insertOne(item);

        return new JSONObject();
    }
}
