package net.swofty.commons.skyblock.auctions;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuctionItem {
    private UUID uuid;
    private UUID originator;
    private UnderstandableSkyBlockItem item;
    private long endTime;

    private boolean isBin;
    private Integer startingPrice;

    private List<Bid> bids;

    public AuctionItem() {
        this.bids = new ArrayList<>();
    }

    public AuctionItem(UnderstandableSkyBlockItem item, UUID originator, long endTime, boolean isBin, Long startingPrice) {
        this.uuid = UUID.randomUUID();
        this.originator = originator;
        this.item = item;
        this.endTime = endTime;
        this.isBin = isBin;
        this.startingPrice = Math.toIntExact(startingPrice);

        this.bids = new ArrayList<>();
    }

    public Document toDocument() {
        return new Document()
                .append("_id", uuid.toString())
                .append("originator", originator.toString())
                .append("item", new UnderstandableSkyBlockItemSerializer().serialize(item))
                .append("end", endTime)
                .append("bin", isBin)
                .append("starting-price", startingPrice)
                .append("bids", bids.stream().map(Bid::toString).toList());
    }

    public static AuctionItem fromDocument(Document document) {
        AuctionItem item = new AuctionItem();
        item.setUuid(UUID.fromString(document.getString("_id")));
        item.setOriginator(UUID.fromString(document.getString("originator")));
        item.setItem(new UnderstandableSkyBlockItemSerializer().deserialize(document.getString("item")));
        item.setEndTime(document.getLong("end"));
        item.setBin(document.getBoolean("bin"));
        item.setStartingPrice(document.getInteger("starting-price"));
        item.setBids(document.getList("bids", String.class).stream().map(Bid::fromString).toList());
        return item;
    }

    public record Bid(Long timestamp, UUID uuid, Long value) {
        public @NotNull String toString() {
            return timestamp + "," + uuid + "," + value;
        }

        public static Bid fromString(String string) {
            String[] parts = string.split(",");
            return new Bid(Long.parseLong(parts[0]), UUID.fromString(parts[1]), Long.parseLong(parts[2]));
        }
    }
}
