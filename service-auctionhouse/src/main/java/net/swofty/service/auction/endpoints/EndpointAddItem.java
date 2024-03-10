package net.swofty.service.auction.endpoints;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.types.generic.auction.AuctionItem;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Map;

public class EndpointAddItem implements ServiceEndpoint {
    @Override
    public String channel() {
        return "add-item";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        AuctionItem item = (AuctionItem) messageData.get("item");
        AuctionCategories category = (AuctionCategories) messageData.get("category");
        Document document = item.toDocument();
        document.put("category", category.name());

        if (AuctionActiveDatabase.collection.find(new Document("_id", document.get("_id"))).first() != null) {
            AuctionActiveDatabase.collection.updateOne(new Document("_id", document.get("_id")), new Document("$set", item));
        } else {
            AuctionActiveDatabase.collection.insertOne(document);
        }

        return new JSONObject().put("uuid", document.get("_id")).toMap();
    }
}
