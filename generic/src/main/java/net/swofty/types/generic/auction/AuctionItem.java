package net.swofty.types.generic.auction;

import lombok.Getter;
import lombok.Setter;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class AuctionItem {
    private UUID originator;
    private SkyBlockItem item;
    private long endTime;

    private boolean isBin;
    private Long startingPrice;

    private Map<UUID, Long> bids;

    public Document toDocument() {
        return new Document()
                .append("originator", originator.toString())
                .append("item", SkyBlockItemSerializer.serialize(item))
                .append("end", endTime)
                .append("bin", isBin)
                .append("starting-price", startingPrice)
                .append("bids", bids);
    }

    public static AuctionItem fromDocument(Document document) {
        AuctionItem item = new AuctionItem();
        item.setOriginator(UUID.fromString(document.getString("originator")));
        item.setItem(SkyBlockItemDeserializer.deserialize((JSONObject) document.get("item")));
        item.setEndTime(document.getLong("end"));
        item.setBin(document.getBoolean("bin"));
        item.setStartingPrice(document.getLong("starting-price"));
        item.setBids((Map<UUID, Long>) document.get("bids"));
        return item;
    }
}
