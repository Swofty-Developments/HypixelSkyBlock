package net.swofty.commons.protocol.objects.auctions;

import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.AuctionItemSerializer;

import java.util.UUID;

public class AuctionFetchItemProtocolObject extends ProtocolObject<
        AuctionFetchItemProtocolObject.AuctionFetchItemMessage,
        AuctionFetchItemProtocolObject.AuctionFetchItemResponse> {

    @Override
    public Serializer<AuctionFetchItemMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(AuctionFetchItemMessage value) {
                return value.uuid.toString();
            }

            @Override
            public AuctionFetchItemMessage deserialize(String json) {
                return new AuctionFetchItemMessage(UUID.fromString(json));
            }

            @Override
            public AuctionFetchItemMessage clone(AuctionFetchItemMessage value) {
                return new AuctionFetchItemMessage(value.uuid);
            }
        };
    }

    @Override
    public Serializer<AuctionFetchItemResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(AuctionFetchItemResponse value) {
                return new AuctionItemSerializer<>().serialize(value.item);
            }

            @Override
            public AuctionFetchItemResponse deserialize(String json) {
                return new AuctionFetchItemResponse(new AuctionItemSerializer<>().deserialize(json));
            }

            @Override
            public AuctionFetchItemResponse clone(AuctionFetchItemResponse value) {
                return new AuctionFetchItemResponse(value.item);
            }
        };
    }

    public record AuctionFetchItemMessage(UUID uuid) { }

    public record AuctionFetchItemResponse(AuctionItem item) { }
}
