package net.swofty.types.generic.serializer;

import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.auction.AuctionItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AuctionItemListSerializer implements Serializer<List<AuctionItem>> {
    private final AuctionItemSerializer<AuctionItem> auctionItemSerializer = new AuctionItemSerializer<>(AuctionItem.class);

    @Override
    public String serialize(List<AuctionItem> value) {
        JSONArray jsonArray = new JSONArray();
        for (AuctionItem auctionItem : value) {
            jsonArray.put(new JSONObject(auctionItemSerializer.serialize(auctionItem)));
        }
        return new JSONObject(new HashMap<String, Object>() {{
            put("auctionItems", jsonArray);
        }}).toString();
    }

    @Override
    public List<AuctionItem> deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("auctionItems");
        List<AuctionItem> auctionItems = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            auctionItems.add(auctionItemSerializer.deserialize(jsonArray.getJSONObject(i).toString()));
        }
        return auctionItems;
    }

    @Override
    public List<AuctionItem> clone(List<AuctionItem> value) {
        return value.stream()
                .map(auctionItemSerializer::clone)
                .collect(Collectors.toList());
    }
}