package net.swofty.commons.protocol.objects.auctions;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class AuctionAddItemProtocolObject extends ProtocolObject
        <AuctionAddItemProtocolObject.AuctionAddItemMessage,
        AuctionAddItemProtocolObject.AuctionAddItemResponse> {
    private static final Serializer<AuctionAddItemMessage> SERIALIZER =
            new JacksonSerializer<>(AuctionAddItemMessage.class);
    private static final Serializer<AuctionAddItemResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(AuctionAddItemResponse.class);

    @Override

    public Serializer<AuctionAddItemMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<AuctionAddItemResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record AuctionAddItemMessage(AuctionItem item, AuctionCategories category) {}

    public record AuctionAddItemResponse(UUID uuid, boolean success, @Nullable String error) {}
}
