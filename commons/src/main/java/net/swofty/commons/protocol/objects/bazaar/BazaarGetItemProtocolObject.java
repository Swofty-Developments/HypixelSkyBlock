package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class BazaarGetItemProtocolObject extends ProtocolObject<
        BazaarGetItemProtocolObject.BazaarGetItemMessage,
        BazaarGetItemProtocolObject.BazaarGetItemResponse> {

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
                return value.item.serialize();
            }

            @Override
            public BazaarGetItemResponse deserialize(String json) {
                return new BazaarGetItemResponse(BazaarItem.deserialize(json));
            }

            @Override
            public BazaarGetItemResponse clone(BazaarGetItemResponse value) {
                return new BazaarGetItemResponse(BazaarItem.fromDocument(value.item.toDocument()));
            }
        };
    }

    public record BazaarGetItemMessage(String itemName) {}

    public record BazaarGetItemResponse(BazaarItem item) {}
}
