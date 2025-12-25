package net.swofty.commons.protocol.objects.auctions;

import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.AuctionItemSerializer;
import org.json.JSONObject;

import java.util.UUID;

public class AuctionAddItemProtocolObject extends ProtocolObject
        <AuctionAddItemProtocolObject.AuctionAddItemMessage,
        AuctionAddItemProtocolObject.AuctionAddItemResponse> {

    @Override
    public Serializer<AuctionAddItemMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(AuctionAddItemMessage value) {
                JSONObject json = new JSONObject();
                json.put("item", new AuctionItemSerializer<>().serialize(value.item));
                json.put("category", value.category.name());
                return json.toString();
            }

            @Override
            public AuctionAddItemMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                AuctionItem item = new AuctionItemSerializer<>().deserialize(jsonObject.getString("item"));
                AuctionCategories category = AuctionCategories.valueOf(jsonObject.getString("category"));
                return new AuctionAddItemMessage(item, category);
            }

            @Override
            public AuctionAddItemMessage clone(AuctionAddItemMessage value) {
                return new AuctionAddItemMessage(value.item, value.category);
            }
        };
    }

    @Override
    public Serializer<AuctionAddItemResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(AuctionAddItemResponse value) {
                return value.uuid.toString();
            }

            @Override
            public AuctionAddItemResponse deserialize(String json) {
                return new AuctionAddItemResponse(UUID.fromString(json));
            }

            @Override
            public AuctionAddItemResponse clone(AuctionAddItemResponse value) {
                return new AuctionAddItemResponse(value.uuid);
            }
        };
    }

    public record AuctionAddItemMessage(AuctionItem item, AuctionCategories category) { }

    public record AuctionAddItemResponse(UUID uuid) { }
}
