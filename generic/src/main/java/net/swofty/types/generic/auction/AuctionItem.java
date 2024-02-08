package net.swofty.types.generic.auction;

import lombok.Getter;
import lombok.Setter;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

    public List<String> getLore() {
        return getLore(null);
    }

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
        toReturn.add(" ");

        if (player != null && originator.equals(player.getUuid())) {
            toReturn.add("§aThis is your own auction!");
            toReturn.add(" ");
        }

        toReturn.add("§eClick to inspect!");
        return toReturn;
    }

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
