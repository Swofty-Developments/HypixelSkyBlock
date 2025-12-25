package net.swofty.commons.protocol.objects.auctions;

import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.skyblock.auctions.AuctionsFilter;
import net.swofty.commons.skyblock.auctions.AuctionsSorting;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.AuctionItemListSerializer;
import org.json.JSONObject;

import java.util.List;

public class AuctionFetchItemsProtocolObject extends ProtocolObject<
        AuctionFetchItemsProtocolObject.AuctionFetchItemsMessage,
        AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse> {

    @Override
    public Serializer<AuctionFetchItemsMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(AuctionFetchItemsMessage value) {
                JSONObject json = new JSONObject();
                json.put("sorting", value.sorting.name());
                json.put("filter", value.filter.name());
                json.put("category", value.category.name());
                return json.toString();
            }

            @Override
            public AuctionFetchItemsMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                AuctionsSorting sorting = AuctionsSorting.valueOf(jsonObject.getString("sorting"));
                AuctionsFilter filter = AuctionsFilter.valueOf(jsonObject.getString("filter"));
                AuctionCategories category = AuctionCategories.valueOf(jsonObject.getString("category"));
                return new AuctionFetchItemsMessage(sorting, filter, category);
            }

            @Override
            public AuctionFetchItemsMessage clone(AuctionFetchItemsMessage value) {
                return new AuctionFetchItemsMessage(value.sorting, value.filter, value.category);
            }
        };
    }

    @Override
    public Serializer<AuctionFetchItemsResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(AuctionFetchItemsResponse value) {
                return new AuctionItemListSerializer().serialize(value.items);
            }

            @Override
            public AuctionFetchItemsResponse deserialize(String json) {
                return new AuctionFetchItemsResponse(new AuctionItemListSerializer().deserialize(json));
            }

            @Override
            public AuctionFetchItemsResponse clone(AuctionFetchItemsResponse value) {
                return new AuctionFetchItemsResponse(value.items);
            }
        };
    }

    public record AuctionFetchItemsMessage(
            AuctionsSorting sorting,
            AuctionsFilter filter,
            AuctionCategories category
    ) { }

    public record AuctionFetchItemsResponse(
            List<AuctionItem> items
    ) { }
}
