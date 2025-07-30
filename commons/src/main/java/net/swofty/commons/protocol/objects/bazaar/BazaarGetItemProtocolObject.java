package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.UUID;

public class BazaarGetItemProtocolObject extends ProtocolObject<
        BazaarGetItemProtocolObject.BazaarGetItemMessage,
        BazaarGetItemProtocolObject.BazaarGetItemResponse> {

    private static final Gson gson = new Gson();

    @Override
    public Serializer<BazaarGetItemMessage> getSerializer() {
        return new Serializer<BazaarGetItemMessage>() {
            @Override
            public String serialize(BazaarGetItemMessage value) {
                return value.itemName;
            }

            @Override
            public BazaarGetItemMessage deserialize(String json) {
                return new BazaarGetItemMessage(json);
            }

            @Override
            public BazaarGetItemMessage clone(BazaarGetItemMessage value) {
                return new BazaarGetItemMessage(value.itemName);
            }
        };
    }

    @Override
    public Serializer<BazaarGetItemResponse> getReturnSerializer() {
        return new Serializer<BazaarGetItemResponse>() {
            @Override
            public String serialize(BazaarGetItemResponse value) {
                return gson.toJson(value);
            }

            @Override
            public BazaarGetItemResponse deserialize(String json) {
                return gson.fromJson(json, BazaarGetItemResponse.class);
            }

            @Override
            public BazaarGetItemResponse clone(BazaarGetItemResponse value) {
                return new BazaarGetItemResponse(
                        value.itemName,
                        List.copyOf(value.buyOrders),
                        List.copyOf(value.sellOrders)
                );
            }
        };
    }

    public record BazaarGetItemMessage(String itemName) {}

    public record BazaarGetItemResponse(
            String itemName,
            List<OrderRecord> buyOrders,
            List<OrderRecord> sellOrders
    ) {}

    public record OrderRecord(
            UUID playerUUID,
            double price,
            double amount
    ) {}
}