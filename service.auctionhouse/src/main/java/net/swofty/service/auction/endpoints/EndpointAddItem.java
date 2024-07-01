package net.swofty.service.auction.endpoints;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.item.attribute.attributes.ItemAttributeRarity;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.commons.auctions.AuctionItem;
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
        AuctionItem auctionItem = (AuctionItem) messageData.get("auctionItem");
        AuctionCategories category = (AuctionCategories) messageData.get("category");
        Document document = auctionItem.toDocument();
        document.put("category", category.name());

        if (AuctionActiveDatabase.collection.find(new Document("_id", document.get("_id"))).first() != null) {
            AuctionActiveDatabase.collection.updateOne(new Document("_id", document.get("_id")), new Document("$set", auctionItem));
        } else {
            AuctionActiveDatabase.collection.insertOne(document);
        }

        return new JSONObject().put("uuid", document.get("_id")).toMap();
    }
}
