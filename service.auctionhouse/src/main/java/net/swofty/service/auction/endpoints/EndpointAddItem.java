package net.swofty.service.auction.endpoints;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.item.attribute.attributes.ItemAttributeRarity;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.service.auction.AuctionActiveDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.commons.auctions.AuctionItem;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Map;
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
        AuctionCategories category = messageObject.category();
        Document document = auctionItem.toDocument();
        document.put("category", category.name());

        if (AuctionActiveDatabase.collection.find(new Document("_id", document.get("_id"))).first() != null) {
            AuctionActiveDatabase.collection.updateOne(new Document("_id", document.get("_id")), new Document("$set", auctionItem));
        } else {
            AuctionActiveDatabase.collection.insertOne(document);
        }

        return new AuctionAddItemProtocolObject.AuctionAddItemResponse(UUID.fromString((String) document.get("_id")));
    }
}
