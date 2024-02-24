package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.auction.AuctionItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AuctionItemListSerializer implements Serializer<List<AuctionItem>> {
    private final AuctionItemSerializer<AuctionItem> auctionItemSerializer = new AuctionItemSerializer<>(AuctionItem.class);

    @Override
    public String serialize(List<AuctionItem> value) {
        JSONArray jsonArray = new JSONArray();
        value.forEach(auctionItem -> jsonArray.put(auctionItemSerializer.serialize(auctionItem)));
        return jsonArray.toString();
    }

    @Override
    public List<AuctionItem> deserialize(String json) {
        JSONArray objects = new JSONArray(json);
        if (objects.isEmpty()) {
            return new ArrayList<>();
        }

        return objects.toList().stream()
                .map(object -> {
                    JSONObject jsonObject = new JSONObject((HashMap<String, Object>) object);
                    return auctionItemSerializer.deserialize(jsonObject.toString());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionItem> clone(List<AuctionItem> value) {
        return value.stream()
                .map(auctionItemSerializer::clone)
                .collect(Collectors.toList());
    }
}