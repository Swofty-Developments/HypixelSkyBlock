package net.swofty.types.generic.auction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;

import java.beans.Transient;
import java.util.*;

@Getter
@Setter
public class AuctionItem {
    private UUID uuid;
    private UUID originator;
    private SkyBlockItem item;
    private long endTime;

    private boolean isBin;
    private Integer startingPrice;

    private List<Bid> bids;

    public AuctionItem() {
        this.bids = new ArrayList<>();
    }

    public AuctionItem(SkyBlockItem item, UUID originator, long endTime, boolean isBin, Long startingPrice) {
        this.uuid = UUID.randomUUID();
        this.originator = originator;
        this.item = item;
        this.endTime = endTime;
        this.isBin = isBin;
        this.startingPrice = Math.toIntExact(startingPrice);

        this.bids = new ArrayList<>();
    }
    @JsonIgnore
    @Transient
    public List<String> getLore() {
        return getLore(null);
    }

    @JsonIgnore
    @Transient
    public List<String> getLore(SkyBlockPlayer player) {
        List<String> toReturn = new ArrayList<>();

        if (player == null) {
            new NonPlayerItemUpdater(item).getUpdatedItem().build().getLore().forEach(loreEntry -> {
                toReturn.add(StringUtility.getTextFromComponent(loreEntry));
            });
        } else {
            PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build().getLore().forEach(loreEntry -> {
                toReturn.add(StringUtility.getTextFromComponent(loreEntry));
            });
        }

        toReturn.add("§8§m----------------------");
        toReturn.add("§7Seller: " + SkyBlockPlayer.getDisplayName(originator));

        if (isBin) {
            toReturn.add("§7Buy it now: §6" + startingPrice + " coins");
        } else {
            if (bids.isEmpty()) {
                toReturn.add("§7Starting bid: §6" + startingPrice + " coins");
            } else {
                toReturn.add("§7Bids: §a" + bids.size() + " bid" + (bids.size() == 1 ? "" : "s"));
                toReturn.add(" ");

                Bid topBid = bids.stream().max(Comparator.comparing(Bid::value)).orElse(null);

                toReturn.add("§7Top bid: §6" + topBid.value + " coins");
                toReturn.add("§7Bidder: " + SkyBlockPlayer.getDisplayName(topBid.uuid()));
            }
        }

        if (player != null) {
            if (originator.equals(player.getUuid())) {
                toReturn.add(" ");
                toReturn.add("§aThis is your own auction!");
            } else {
                CoopDatabase.Coop viewerCoop = CoopDatabase.getFromMember(player.getUuid());
                if (viewerCoop != null && viewerCoop.members().contains(originator)) {
                    toReturn.add(" ");
                    toReturn.add("§aThis is a coop member's auction!");
                }
            }
        }

        toReturn.add(" ");
        if (isBin && !bids.isEmpty()) {
            toReturn.add("§7Status: §aPurchased");
        } else if (endTime > System.currentTimeMillis()) {
            toReturn.add("§7Ends in: §e" + StringUtility.formatTimeLeft(endTime - System.currentTimeMillis()));
        } else {
            toReturn.add("§7Status: §aEnded!");
        }
        toReturn.add(" ");

        toReturn.add("§eClick to inspect!");
        return toReturn;
    }

    public Document toDocument() {
        return new Document()
                .append("_id", uuid.toString())
                .append("originator", originator.toString())
                .append("item", SkyBlockItemSerializer.serialize(item))
                .append("end", endTime)
                .append("bin", isBin)
                .append("starting-price", startingPrice)
                .append("bids", bids.stream().map(Bid::toString).toList());
    }

    public static AuctionItem fromDocument(Document document) {
        AuctionItem item = new AuctionItem();
        item.setUuid(UUID.fromString(document.getString("_id")));
        item.setOriginator(UUID.fromString(document.getString("originator")));
        item.setItem(SkyBlockItemDeserializer.deserialize((Map<String, Object>) document.get("item")));
        item.setEndTime(document.getLong("end"));
        item.setBin(document.getBoolean("bin"));
        item.setStartingPrice(document.getInteger("starting-price"));
        item.setBids(document.getList("bids", String.class).stream().map(Bid::fromString).toList());
        return item;
    }

    public record Bid(Long timestamp, UUID uuid, Long value) {
        public String toString() {
            return timestamp + "," + uuid + "," + value;
        }

        public static Bid fromString(String string) {
            String[] parts = string.split(",");
            return new Bid(Long.parseLong(parts[0]), UUID.fromString(parts[1]), Long.parseLong(parts[2]));
        }
    }
}
